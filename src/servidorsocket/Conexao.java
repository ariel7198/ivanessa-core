package servidorsocket;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    String serverName = "localhost"; //caminho do servidor
    String mydataBase = "iFound"; //nome do banco
    String url = "jdbc:mysql://" + serverName + "/" + mydataBase;
    String username = "root"; // nome do usuario
    String password = "root"; //senha
    Connection conexao;

    Conexao() throws SQLException {
        conexao = DriverManager.getConnection(url, username, password);
    }

}
