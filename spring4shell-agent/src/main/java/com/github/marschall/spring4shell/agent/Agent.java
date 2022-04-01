package com.github.marschall.spring4shell.agent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {

	public static void agentmain(String argString, Instrumentation instrumentation) {
		instrumentation.addTransformer(new WebDataBinderPatcher(), true);
	}
	
	public static void premain(String argString, Instrumentation instrumentation) {
		instrumentation.addTransformer(new WebDataBinderPatcher(), true);
	}
	
	static final class WebDataBinderPatcher implements ClassFileTransformer {

		private static final String WEB_DATA_BINDER = "org/springframework/web/bind/WebDataBinder";

		@Override
		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
				ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
			
			if (className.equals(WEB_DATA_BINDER)) {
				return loadPatchedClass();
			} else {
				return null;
			}
		}

		@Override
		public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined,
				ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
			if (className.equals(WEB_DATA_BINDER)) {
				return loadPatchedClass();
			} else {
				return null;
			}
		}
		
		private static byte[] loadPatchedClass() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			try (InputStream input = Agent.class.getClassLoader().getResourceAsStream("WebDataBinder.class")) {
				input.transferTo(output);
			} catch (IOException e) {
				throw new IllegalStateException("could not load patch", e);
			}
			return output.toByteArray();
		}
		
	}

}
