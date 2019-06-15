package Client;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Aviadjo on 3/3/2017.
 */
public interface IClientStrategy {
    void clientStrategy(InputStream inFromServer, OutputStream outToServer);
}
