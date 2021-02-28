package com.coffeebeans.cdk;

import com.coffeebeans.cdk.stack.VpcStack;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Tags;

public final class Application {

  public static void main(final String... args) {
    final App app = new App();

    final VpcStack vpcStack = new VpcStack(app, "CreateVPCStack");

    Tags.of(vpcStack).add("env", "dev");

    app.synth();
  }
}
