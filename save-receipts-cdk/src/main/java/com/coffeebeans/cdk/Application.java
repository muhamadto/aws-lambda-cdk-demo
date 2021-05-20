package com.coffeebeans.cdk;

import com.coffeebeans.cdk.stack.VpcStack;
import software.amazon.awscdk.core.App;

public final class Application {

  public static void main(final String... args) {
    final App app = new App();

    new VpcStack(app, "CreateVPCStack");

    app.synth();
  }
}
