package com.github.marschall.spring4shell.agent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * Java agent that replaces {@code org.springframework.web.bind.WebDataBinder}
 * with a patched version.
 */
public class Agent {

	private static final String WEB_DATA_BINDER = "org.springframework.web.bind.WebDataBinder";

	/**
	 * Called when the agent is started after VM startup.
	 * 
	 * @param agentArgs agent argument line
	 * @param instrumentation {@link Instrumentation} provided by the JVM
	 */
	public static void agentmain(String agentArgs, Instrumentation instrumentation) {
		register(instrumentation);
	}

	/**
	 * Called before {@code main(String[])} when the agent is specified on the command line
	 * using {@code -javaagent:<jarpath>}.
	 * 
	 * @param agentArgs agent argument line
	 * @param instrumentation {@link Instrumentation} provided by the JVM
	 */
	public static void premain(String agentArgs, Instrumentation instrumentation) {
		register(instrumentation);
	}

	private static void register(Instrumentation instrumentation) {
		// first redefine all already loaded classes
		redefineLoadedClass(instrumentation);
		// then transform all that are loaded after this point
		instrumentation.addTransformer(new WebDataBinderPatcher(), instrumentation.isRetransformClassesSupported());
	}

	private static void redefineLoadedClass(Instrumentation instrumentation) {
		List<Class<?>> classes = new ArrayList<>(2);
		for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
			if (clazz.getName().equals(WEB_DATA_BINDER)) {
				classes.add(clazz);
			}
		}
		if (!classes.isEmpty()) {
			byte[] byteCode = loadPatchedClass();
			ClassDefinition[] classDefinitions = new ClassDefinition[classes.size()];
			for (int i = 0; i < classes.size(); i++) {
				Class<?> clazz = classes.get(i);
				classDefinitions[i] = new ClassDefinition(clazz, byteCode);
			}
			try {
				instrumentation.redefineClasses(classDefinitions);
			} catch (ClassNotFoundException | UnmodifiableClassException e) {
				throw new IllegalStateException("could not redefine class", e);
			}
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

	static final class WebDataBinderPatcher implements ClassFileTransformer {

		private static final String JVM_WEB_DATA_BINDER = "org/springframework/web/bind/WebDataBinder";

		@Override
		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
				ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

			if (className.equals(JVM_WEB_DATA_BINDER)) {
				return loadPatchedClass();
			} else {
				return null;
			}
		}

		@Override
		public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined,
				ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
			if (className.equals(JVM_WEB_DATA_BINDER)) {
				return loadPatchedClass();
			} else {
				return null;
			}
		}

	}

}
