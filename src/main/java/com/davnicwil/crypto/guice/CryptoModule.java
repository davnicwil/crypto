package com.davnicwil.crypto.guice;

import com.davnicwil.crypto.RandomStringGenerator;
import com.davnicwil.crypto.impl.RandomStringGeneratorImpl;
import com.google.inject.AbstractModule;

public class CryptoModule extends AbstractModule {
    
    protected void configure() {
        bind(RandomStringGenerator.class).to(RandomStringGeneratorImpl.class);
    }
}
