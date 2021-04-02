package silva.emisvaldo.baeldung.corejava.modules.exceptions.trywithresource;

public class MyResource implements AutoCloseable{

	@Override
	public void close() throws Exception {
		System.out.println("Closed MyResource");
	}
}
