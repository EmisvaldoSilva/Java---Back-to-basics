package silva.emisvaldo.baeldung.corejava.modules.exceptions.trywithresource;

public class AutoCloseableResourcesFirst implements AutoCloseable{
	public AutoCloseableResourcesFirst() {
		System.out.println("Constructor -> AutoCloseableResourcesFirst");
	}
	
	public void doSomething() {
		System.out.println("Something -> AutoCloseableResourcesFirst");
	}

	@Override
	public void close() throws Exception {
		System.out.println("Closed AutoCloseableResourcesFirst");		
	}

}
