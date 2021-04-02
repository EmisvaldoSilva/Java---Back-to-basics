package silva.emisvaldo.baeldung.corejava.modules.exceptions.trywithresource;

public class AutoClosableMain {

	public static void main(String[] args) throws Exception {
		orderOfClosingResources();
	}

	private static void orderOfClosingResources() throws Exception{
		try (AutoCloseableResourcesFirst af = new AutoCloseableResourcesFirst();
				AutoCloseableResourcesSecund as = new AutoCloseableResourcesSecund()){
			af.doSomething();
			as.doSomething();
		}
	}
}
