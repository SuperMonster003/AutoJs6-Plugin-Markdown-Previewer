package android.print;

import java.util.concurrent.CountDownLatch;

/** Test-only access to PrintDocumentAdapter callback constructors hidden from application code. */
public final class TestPrintCallbacks {

    private TestPrintCallbacks() {
    }

    public static final class Layout extends PrintDocumentAdapter.LayoutResultCallback {
        public final CountDownLatch finished = new CountDownLatch(1);
        public volatile PrintDocumentInfo info;
        public volatile CharSequence error;
        public volatile boolean cancelled;

        @Override
        public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
            this.info = info;
            finished.countDown();
        }

        @Override
        public void onLayoutFailed(CharSequence error) {
            this.error = error;
            finished.countDown();
        }

        @Override
        public void onLayoutCancelled() {
            cancelled = true;
            finished.countDown();
        }
    }

    public static final class Write extends PrintDocumentAdapter.WriteResultCallback {
        public final CountDownLatch finished = new CountDownLatch(1);
        public volatile PageRange[] pages;
        public volatile CharSequence error;
        public volatile boolean cancelled;

        @Override
        public void onWriteFinished(PageRange[] pages) {
            this.pages = pages;
            finished.countDown();
        }

        @Override
        public void onWriteFailed(CharSequence error) {
            this.error = error;
            finished.countDown();
        }

        @Override
        public void onWriteCancelled() {
            cancelled = true;
            finished.countDown();
        }
    }
}
