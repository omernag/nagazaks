package Server;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Aviadjo on 3/2/2017.
 */
public interface IServerStrategy {
    void serverStrategy(InputStream inFromClient, OutputStream outToClient);
}
