import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class LerArq {

	public void LerArquivo() throws IOException {
		InputStream is = new FileInputStream("C:\\Temp\\Configuracao\\config.txt");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String PathProcessado = "";
		String PathNaoProcessado = "";

		try {
			String s = null;
			while ((s = br.readLine()) != null) {

				if (s.contains("=")) {
					String[] ConfigSplit = (s.split("=")); // divide a string
					String nome = ConfigSplit[0];
					String caminho = ConfigSplit[1];

					if (nome.equals("Processado")) {
						PathProcessado = caminho;
						new File(PathProcessado).mkdir();
					} else if (nome.equals("NaoProcessado")) {
						PathNaoProcessado = caminho;
						new File(PathNaoProcessado).mkdir();
					} else {
						throw new Exception("Arquivo de configuração invalido. Nome usado: " + nome);// mostra erro se a
																										// pasta estiver
																										// errada
					}
				} else {
					throw new Exception("Arquivo CONFIG sem o separador = ");
				}
			} /*
				 * else { throw new Exception("Config esta em branco"); }
				 */
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		br.close();

	}
}