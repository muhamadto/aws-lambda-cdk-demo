package com.myorg;

import software.amazon.awscdk.core.App;

public final class SaveReceiptFunctionApp {
    public static void main(final String[] args) {
        App app = new App();

        new SaveReceiptFunctionStack(app, "SaveReceiptFunctionStack");

        app.synth();
    }
}
