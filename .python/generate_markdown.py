# -*- coding: utf-8 -*-
CHECK_MODE = False
CHECK_ERRORS = []
GENERATED_PATHS = set()

import json
import re
import xml.etree.ElementTree as ElementTree
from pathlib import Path


LANGUAGE_CODES = [
    "zh-Hans",
    "zh-Hant-HK",
    "zh-Hant-TW",
    "en",
    "fr",
    "es",
    "ja",
    "ko",
    "ru",
    "ar",
]
LANGUAGE_CODE_DEFAULT = "zh-Hans"
README_COMPAT_ROOT_LANGUAGES = set()
ANDROID_CHANGELOG_ALIASES = {
    "zh-Hans": ["zh", "zh-Hans"],
    "zh-Hant-HK": ["zh-rHK", "zh-Hant-HK"],
    "zh-Hant-TW": ["zh-rTW", "zh-Hant-TW"],
}
ANDROID_RESOURCE_QUALIFIERS = {
    "zh-Hans": "zh",
    "zh-Hant-HK": "zh-rHK",
    "zh-Hant-TW": "zh-rTW",
    "en": "en",
    "fr": "fr",
    "es": "es",
    "ja": "ja",
    "ko": "ko",
    "ru": "ru",
    "ar": "ar",
}
PROHIBITED_SYMBOL_PATTERN = re.compile(
    r"[‐-‧　-〿・︐-﹯＀-￯]",
)
ANDROID_RESOURCE_ALLOWED_SYMBOLS = ("’", "…")

# Plugin Center instruction copy lives in its own keys and never borrows README fields
# (D:/idea-projects/AUTOJS6_PLUGIN_INSTRUCTION_AGENTS.md).
INSTRUCTION_KEY_PREFIX = "instruction_"
INSTRUCTION_LIST_KEYS = [
    "instruction_start_steps",
    "instruction_features",
    "instruction_requirements",
]
INSTRUCTION_FAQ_KEY = "instruction_faq"
INSTRUCTION_REQUIRED_KEYS = [
    "instruction_p_what",
    "instruction_h3_start",
    "instruction_start_steps",
    "instruction_h3_requirements",
    "instruction_requirements",
]


def project_root() -> Path:
    return Path(__file__).resolve().parents[1]


ROOT = project_root()
README_DIR = ROOT / ".readme"
CHANGELOG_DIR = ROOT / ".changelog"
ANDROID_CHANGELOG_DIR = ROOT / "app" / "src" / "main" / "assets" / "doc"
ANDROID_RES_DIR = ROOT / "app" / "src" / "main" / "res"
VERSION_PROPERTIES = ROOT / "version.properties"


def validate_source_layout():
    expected_changelog_files = {
        "template_changelog.md",
        *(f"lang_{code}.json" for code in LANGUAGE_CODES),
    }
    changelog_entries = list(CHANGELOG_DIR.iterdir())
    actual_changelog_files = {path.name for path in changelog_entries}
    invalid_entry_types = sorted(path.name for path in changelog_entries if not path.is_file())
    if actual_changelog_files != expected_changelog_files or invalid_entry_types:
        missing = sorted(expected_changelog_files - actual_changelog_files)
        unexpected = sorted(actual_changelog_files - expected_changelog_files)
        raise ValueError(
            "Invalid .changelog entries; "
            f"missing={missing}, unexpected={unexpected}, non_files={invalid_entry_types}",
        )

    root_changelogs = sorted(path.name for path in ROOT.glob("CHANGELOG*.md"))
    if root_changelogs:
        raise ValueError(f"Root changelog files are not allowed: {root_changelogs}")

    root_compat_readmes = sorted(path.name for path in ROOT.glob("README-*.md"))
    if root_compat_readmes:
        raise ValueError(f"Root compatibility README files are not allowed: {root_compat_readmes}")


def validate_symbols(text: str, source, allowed_symbols=()):
    if allowed_symbols:
        text = "".join(char for char in text if char not in allowed_symbols)
    match = PROHIBITED_SYMBOL_PATTERN.search(text)
    if match:
        code_point = f"U+{ord(match.group()):04X}"
        raise ValueError(f"Prohibited full-width symbol {match.group()!r} ({code_point}) in {source}")


def validate_json_symbols(value, source):
    if isinstance(value, dict):
        for key, item in value.items():
            validate_symbols(str(key), source)
            validate_json_symbols(item, source)
    elif isinstance(value, list):
        for item in value:
            validate_json_symbols(item, source)
    elif isinstance(value, str):
        validate_symbols(value, source)


def load_json(path: Path):
    text = path.read_text(encoding="utf-8")
    validate_symbols(text, path.relative_to(ROOT))
    value = json.loads(text)
    validate_json_symbols(value, path.relative_to(ROOT))
    return value


def load_template(path: Path) -> str:
    text = path.read_text(encoding="utf-8")
    validate_symbols(text, path.relative_to(ROOT))
    return text


def render_template(text: str, values: dict) -> str:
    def repl(match):
        key = match.group(1).strip()
        if key not in values:
            raise KeyError(f"Missing template value: {key}")
        return str(values[key])

    return re.sub(r"\{\{\s*([A-Za-z0-9_$.-]+)\s*\}\}", repl, text)


def render_dynamic(value, values: dict):
    if isinstance(value, dict):
        return {k: render_dynamic(v, values) for k, v in value.items()}
    if isinstance(value, list):
        return [render_dynamic(v, values) for v in value]
    if isinstance(value, str):
        return render_template(value, values)
    return value


def markdown_link(label, url):
    return f"[{label}]({url})"


def bullet_list(items):
    return "\n".join(f"- {item}" for item in items)


def numbered_list(items):
    return "\n".join(f"{index}. {item}" for index, item in enumerate(items, start=1))


def faq_block(items):
    blocks = [f"**{item['question']}**\n\n{item['answer']}" for item in items]
    return "\n\n".join(blocks)


def changelog_version_name() -> str:
    properties = {}
    for line in VERSION_PROPERTIES.read_text(encoding="utf-8").splitlines():
        if not line or line.lstrip().startswith("#") or "=" not in line:
            continue
        key, value = line.split("=", 1)
        properties[key.strip()] = value.strip()
    version_name = properties.get("VERSION_NAME")
    if not version_name:
        raise ValueError("VERSION_NAME is missing from version.properties")
    return version_name if version_name.startswith("v") else f"v{version_name}"


def shape_of(value):
    if isinstance(value, dict):
        return ("dict", tuple(sorted(value)))
    if isinstance(value, list):
        return ("list", len(value), tuple(shape_of(item) for item in value))
    return type(value).__name__


def validate_instruction_sources(raw_languages: dict):
    reference = {k: v for k, v in raw_languages[LANGUAGE_CODE_DEFAULT].items() if k.startswith(INSTRUCTION_KEY_PREFIX)}
    for code, content in raw_languages.items():
        instruction = {k: v for k, v in content.items() if k.startswith(INSTRUCTION_KEY_PREFIX)}
        for key in INSTRUCTION_REQUIRED_KEYS:
            if key not in instruction:
                raise ValueError(f"Instruction language {code!r} is missing {key!r}")
        if set(instruction) != set(reference):
            raise ValueError(f"Instruction keys of {code!r} differ from {LANGUAGE_CODE_DEFAULT!r}: {sorted(set(instruction) ^ set(reference))}")
        for key, value in instruction.items():
            if shape_of(value) != shape_of(reference[key]):
                raise ValueError(f"Instruction field {key!r} of {code!r} does not match the {LANGUAGE_CODE_DEFAULT!r} shape")
            if key in INSTRUCTION_LIST_KEYS:
                valid = isinstance(value, list) and value and all(isinstance(item, str) and item for item in value)
            elif key == INSTRUCTION_FAQ_KEY:
                valid = isinstance(value, list) and value and all(
                    isinstance(item, dict) and set(item) == {"q", "a"} and all(isinstance(text, str) and text for text in item.values())
                    for item in value
                )
            else:
                valid = isinstance(value, str) and value
            if not valid:
                raise ValueError(f"Instruction field {key!r} of {code!r} is empty or malformed")
            # UI names are quoted with ASCII double quotes; an odd count means a quote was swallowed on the way in.
            texts = [value] if isinstance(value, str) else [
                text for item in value for text in (item.values() if isinstance(item, dict) else [item])
            ]
            for text in texts:
                if text.count('"') % 2:
                    raise ValueError(f"Instruction field {key!r} of {code!r} has an unpaired double quote: {text[:60]!r}")


def load_languages():
    common = load_json(README_DIR / "common.json")
    raw_languages = {code: load_json(README_DIR / f"lang_{code}.json") for code in LANGUAGE_CODES}
    validate_instruction_sources(raw_languages)
    languages = {}
    changelogs = {}
    expected_version = changelog_version_name()
    expected_version_names = None
    for code in LANGUAGE_CODES:
        raw_lang = raw_languages[code]
        merged_lang = {**common, **raw_lang}
        languages[code] = render_dynamic(merged_lang, merged_lang)

        synopsis = languages[code]["text_plugin_synopsis"]
        if synopsis != synopsis.rstrip():
            raise ValueError(f"Plugin synopsis must not end with whitespace: {code}")
        if synopsis.rstrip().endswith((".", "!", "?", ":", ";")):
            raise ValueError(f"Plugin synopsis must not end with punctuation: {code}")

        raw_changelog = load_json(CHANGELOG_DIR / f"lang_{code}.json")
        changelog_values = {k: v for k, v in raw_changelog.items() if k != "$data"}
        changelog_values = render_dynamic(changelog_values, changelog_values)
        changelog_data = render_dynamic(raw_changelog["$data"], changelog_values)
        version_names = list(changelog_data)
        if not version_names or version_names[0] != expected_version:
            raise ValueError(
                f"Latest changelog version for {code} must be {expected_version!r}: {version_names}",
            )
        if expected_version_names is None:
            expected_version_names = version_names
        elif version_names != expected_version_names:
            raise ValueError(f"Changelog versions are inconsistent for {code}: {version_names}")
        changelogs[code] = {
            "values": changelog_values,
            "data": changelog_data,
        }
    return languages, changelogs


def android_string_value(value: str) -> str:
    return value.replace("\\'", "'").replace('\\"', '"').replace("\\n", "\n")


def validate_android_resource_pair(code: str, languages, values_dir: str, raw_dir: str):
    strings_path = ANDROID_RES_DIR / values_dir / "strings.xml"
    instruction_path = ANDROID_RES_DIR / raw_dir / "plugin_instruction.md"

    strings_text = strings_path.read_text(encoding="utf-8")
    instruction_text = instruction_path.read_text(encoding="utf-8")
    validate_symbols(
        strings_text,
        strings_path.relative_to(ROOT),
        allowed_symbols=ANDROID_RESOURCE_ALLOWED_SYMBOLS,
    )
    validate_symbols(instruction_text, instruction_path.relative_to(ROOT))

    root = ElementTree.fromstring(strings_text)
    element = next(
        (item for item in root.findall("string") if item.get("name") == "plugin_description"),
        None,
    )
    if element is None:
        raise ValueError(f"plugin_description is missing from {strings_path.relative_to(ROOT)}")
    description = android_string_value("".join(element.itertext()))
    synopsis = languages[code]["text_plugin_synopsis"]
    if description != synopsis:
        raise ValueError(
            f"plugin_description differs from text_plugin_synopsis for {code}: "
            f"{description!r} != {synopsis!r}",
        )


def validate_android_resources(languages):
    for code, qualifier in ANDROID_RESOURCE_QUALIFIERS.items():
        validate_android_resource_pair(code, languages, f"values-{qualifier}", f"raw-{qualifier}")
    validate_android_resource_pair("en", languages, "values", "raw")


def format_changelog_items(changelog, limit=None, heading_level=1):
    values = changelog["values"]
    data = changelog["data"]
    heading = "#" * heading_level
    chunks = []
    for index, (version_name, item) in enumerate(data.items()):
        if limit is not None and index >= limit:
            break
        lines = [
            f"{heading} {version_name}",
            "",
            f"###### {item['released_date']}",
            "",
        ]
        for category in ["hint", "feature", "fix", "improvement", "dependency"]:
            for text in item.get(category, []):
                label = values[f"changelog_label_{category}"]
                lines.append(f"* `{label}` {text}")
        chunks.append("\n".join(lines).rstrip())
    return "\n\n".join(chunks).rstrip() + "\n"


def build_language_list(target_code, languages):
    lines = []
    for code in LANGUAGE_CODES:
        content = languages[code]
        label = f"{content['$name']} [{code}]"
        if code == target_code:
            lines.append(f"- {label} # {content['text_current_lowercase']}")
        else:
            url = f"{content['repo_url']}/blob/master/.readme/README-{code}.md"
            lines.append(f"- {markdown_link(label, url)}")
    return "\n".join(lines)


def build_readme_values(code, languages, changelogs):
    content = dict(languages[code])
    content["placeholder_ul_languages_all_supported"] = build_language_list(code, languages)
    content["placeholder_highlights"] = bullet_list(content["highlights"])
    content["placeholder_usage_steps"] = numbered_list(content["usage_steps"])
    content["placeholder_faq"] = faq_block(content["faq"])
    content["placeholder_security_points"] = bullet_list(content["security_points"])
    content["placeholder_latest_release_history"] = format_changelog_items(
        changelogs[code],
        limit=3,
        heading_level=4,
    ).rstrip()
    content["placeholder_read_more_in_changelog_md"] = markdown_link(
        f"CHANGELOG-{code}.md",
        f"{content['repo_url']}/blob/master/app/src/main/assets/doc/CHANGELOG-{code}.md",
    )
    return content


def instruction_faq(items):
    return "\n\n".join(f"**{item['q']}**\n\n{item['a']}" for item in items)


def build_instruction_values(code, languages):
    """Only instruction_* keys reach the instruction template, so it cannot borrow README copy."""
    values = {key: value for key, value in languages[code].items() if key.startswith(INSTRUCTION_KEY_PREFIX)}
    for key in INSTRUCTION_LIST_KEYS:
        if key in values:
            render = numbered_list if key == "instruction_start_steps" else bullet_list
            values[f"placeholder_{key}"] = render(values[key])
    if INSTRUCTION_FAQ_KEY in values:
        values[f"placeholder_{INSTRUCTION_FAQ_KEY}"] = instruction_faq(values[INSTRUCTION_FAQ_KEY])
    return values


def write_text(path: Path, text: str):
    GENERATED_PATHS.add(path)
    if CHECK_MODE:
        if not path.is_file() or path.read_bytes().replace(b"\r\n", b"\n") != text.encode("utf-8"):
            CHECK_ERRORS.append(str(path.relative_to(ROOT)))
        return
    validate_symbols(text, path.relative_to(ROOT))
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8", newline="\n") as file:
        file.write(text)
    print(f"Generated {path.relative_to(ROOT)}")


def generate_readmes(languages, changelogs):
    template = load_template(README_DIR / "template_readme.md")
    for code in LANGUAGE_CODES:
        output = render_template(template, build_readme_values(code, languages, changelogs))
        path = README_DIR / f"README-{code}.md"
        write_text(path, output)
        if code == LANGUAGE_CODE_DEFAULT:
            write_text(ROOT / "README.md", output)
        if code in README_COMPAT_ROOT_LANGUAGES:
            write_text(ROOT / f"README-{code}.md", output)


def generate_changelogs(languages, changelogs):
    template = load_template(CHANGELOG_DIR / "template_changelog.md")
    for code in LANGUAGE_CODES:
        values = dict(languages[code])
        values["placeholder_release_history"] = format_changelog_items(
            changelogs[code],
            heading_level=2,
        ).rstrip()
        output = render_template(template, values)
        names = ANDROID_CHANGELOG_ALIASES.get(code, [code])
        for name in names:
            write_text(ANDROID_CHANGELOG_DIR / f"CHANGELOG-{name}.md", output)
        if code == LANGUAGE_CODE_DEFAULT:
            write_text(ANDROID_CHANGELOG_DIR / "CHANGELOG.md", output)


def generate_instructions(languages):
    """Writes raw-<qualifier>/plugin_instruction.md for every language, plus raw/ as the English default."""
    template = load_template(README_DIR / "template_plugin_instruction.md")
    for code, qualifier in ANDROID_RESOURCE_QUALIFIERS.items():
        output = render_template(template, build_instruction_values(code, languages))
        if "{{" in output or "}}" in output:
            raise ValueError(f"Unrendered placeholder in the {code!r} instruction")
        write_text(ANDROID_RES_DIR / f"raw-{qualifier}" / "plugin_instruction.md", output)
        if code == "en":
            write_text(ANDROID_RES_DIR / "raw" / "plugin_instruction.md", output)


def main():
    if LANGUAGE_CODE_DEFAULT not in LANGUAGE_CODES:
        raise ValueError(f"Default language code {LANGUAGE_CODE_DEFAULT!r} is not in LANGUAGE_CODES")
    validate_source_layout()
    languages, changelogs = load_languages()
    validate_android_resources(languages)
    generate_changelogs(languages, changelogs)
    generate_readmes(languages, changelogs)
    generate_instructions(languages)



def validate_release_sources():
    import xml.etree.ElementTree as ET
    version_text = (ROOT / "version.properties").read_text(encoding="utf-8-sig")
    version = re.search(r"(?m)^VERSION_NAME=(.+)$", version_text).group(1).strip().split("-")[0]
    shape = None
    for code in LANGUAGE_CODES:
        path = CHANGELOG_DIR / f"lang_{code}.json"
        data = json.loads(path.read_text(encoding="utf-8"))["$data"]
        if next(iter(data)) != f"v{version}":
            raise ValueError(f"Current changelog version mismatch: {path}")
        latest = data[f"v{version}"]
        actual = [(key, len(value) if isinstance(value, list) else value) for key, value in latest.items()]
        if shape is None:
            shape = actual
        elif actual != shape:
            raise ValueError(f"Current changelog language shape mismatch: {path}")
    res = ROOT / "app/src/main/res"
    qualifiers = ["", "en", "ar", "es", "fr", "ja", "ko", "ru", "zh", "zh-rHK", "zh-rTW"]
    reference = None
    for qualifier in qualifiers:
        path = res / ("values" + ("-" + qualifier if qualifier else "")) / "strings.xml"
        entries = {item.attrib["name"]: "".join(item.itertext()) for item in ET.parse(path).getroot() if item.tag == "string"}
        if "plugin_description" not in entries:
            raise ValueError(f"Missing localized plugin description: {path}")
        if list(entries) != sorted(entries):
            raise ValueError(f"Unsorted strings: {path}")
        if reference is None:
            reference = entries
        elif qualifier == "en" and any(value != reference[name] for name, value in entries.items() if name in reference):
            raise ValueError("Default and explicit English strings differ")


def cli():
    import argparse
    global CHECK_MODE
    parser = argparse.ArgumentParser(description="Generate localized documentation or check it without writing")
    parser.add_argument("--check", action="store_true")
    CHECK_MODE = parser.parse_args().check
    validate_release_sources()
    main()
    if CHECK_ERRORS:
        raise SystemExit("Generated documentation differs: " + ", ".join(CHECK_ERRORS))
    print(f"{'Checked' if CHECK_MODE else 'Generated'} {len(GENERATED_PATHS)} documentation artifacts")


if __name__ == "__main__":
    cli()
