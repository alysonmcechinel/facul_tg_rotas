import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException {
		LerArq LerArq = new LerArq();
		VerificacaoRota verificar = new VerificacaoRota();
		LerArq.LerArquivo();
		verificar.verificacao();
	}

}