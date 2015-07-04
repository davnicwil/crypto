package com.davnicwil.crypto.guice;

import com.davnicwil.crypto.RandomStringGenerator;
import com.davnicwil.crypto.impl.RandomStringGeneratorImpl;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class CryptoModule extends AbstractModule {

    private int tokenLength;

    public CryptoModule(int tokenLength) {
        this.tokenLength = tokenLength;
    }

    protected void configure() {
        bind(Integer.class).annotatedWith(Names.named("tokenLength")).toInstance(tokenLength);
        bind(RandomStringGenerator.class).to(RandomStringGeneratorImpl.class);
    }
}
