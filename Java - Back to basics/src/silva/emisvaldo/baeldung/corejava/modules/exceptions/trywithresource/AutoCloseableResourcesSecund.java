package silva.emisvaldo.baeldung.corejava.modules.exceptions.trywithresource;

public class AutoCloseableResourcesSecund implements AutoCloseable {
	
	public AutoCloseableResourcesSecund() {
		System.out.println("Constructor -> AutoCloseableResourcesSecund");
	}
	
	public void doSomething() {
		System.out.println("Something -> AutoCloseableResourcesSecund");
	}

	@Override
	public void close() throws Exception {
		System.out.println("Closed AutoCloseableResourcesSecund");		
	}
}
