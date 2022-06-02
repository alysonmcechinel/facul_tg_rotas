import java.awt.font.NumericShaper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.FileNotFoundException;;

public class VerificacaoRota {

	public void verificacao() {
		// thred inicia
		int teste = 0;
		try {
			while (teste == 0) {
				teste++;
				lerArq();

			}

			throw new Exception("Arquivos Processados ou não encontrado");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			// e.printStackTrace(); verificar o erro
			System.exit(0);
		}
	}

	/*---------------------------------------------------------------*/
	public void lerArq() throws Exception {

		int NN = 0, somapeso = 0, nostrailer = 0, conexoestrailer = 0;
		int contsomapeso = 0, contlinha2 = 0, contlinha3 = 0;
		boolean temHeader = false;
		boolean ArquivoCerto = true;
		ArrayList<String> contaNos = new ArrayList<String>();

		File arquivoDiretorio = new File("C:\\Temp\\");

		for (String arquivos : arquivoDiretorio.list()) {

			try {

				// System.out.println("Pensando");
				Thread.sleep(500);
				if (arquivos.startsWith("Rota")) { // identifica e comeca a ler o arquivo

					InputStream is = new FileInputStream("C:\\Temp\\" + arquivos);
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);

					String linha1 = br.readLine();// linha1.length() <= 6
					ArquivoCerto = (linha1 != null && !linha1.isEmpty());

					while (linha1 != null && ArquivoCerto) {
						if (linha1.substring(0, 2).equals("00")) {
							if (linha1.length() == 6) {// verificar não esta funcionando
								if (!temHeader) {
									if (converterHeader(linha1)) {
										NN = Integer.parseInt(linha1.substring(2, 4));
										somapeso = Integer.parseInt(linha1.substring(4, 6));
										temHeader = true;
										// termina a linha de verificacao 1
										linha1 = br.readLine();

									} else {
										ArquivoCerto = false;
										break;
									}
								} else {
									System.out.println("Arquivo " + arquivos + " Não tem Header");
									ArquivoCerto = false;
									break;
								}
							} else {
								ArquivoCerto = false;
								System.out.println("Header invalido");// acima de 6 caracteres no header
								break;
							}

						} else if (linha1.substring(0, 2).equals("01")) {
							if (temHeader) {
								verificalinha2(linha1, ArquivoCerto);
								contlinha2++;
								// Se em cima der certo faz isso
								contaNos = verificaNos(linha1, contaNos);
							} else {
								System.out.println("Arquivo " + arquivos + " Não tem Header");
								ArquivoCerto = false;
								break;
							}
							linha1 = br.readLine();
						} else if (linha1.substring(0, 2).equals("02")) {
							if (temHeader) {
								contsomapeso += (verificasomalinha3(linha1));
								contlinha3++;
							} else {
								System.out.println("Arquivo " + arquivos + " Não tem Header");
								ArquivoCerto = false;
								break;
							}
							linha1 = br.readLine();
						} else if (linha1.substring(0, 2).equals("09")) {
							if (temHeader) {
								conexoestrailer += (verificatrailerconexao(linha1));
								nostrailer += (verificatrailerpesos(linha1));
								// System.out.println(contlinha2 + " / " + contlinha3);
							} else {
								System.out.println("Arquivo " + arquivos + " Não tem Header");
								ArquivoCerto = false;
								break;
							}
							linha1 = br.readLine();
						}

						else {
							System.out.println("Nenhum identificador encontrado");
							ArquivoCerto = false;
						}
					}
					if (ArquivoCerto) {
						if (contaNos.size() == NN) {
							if (contsomapeso == somapeso) {
								if (conexoestrailer == contlinha2) {
									if (nostrailer == contlinha3) {
										System.out.println("Arquivo esta ok");
									} else {
										System.out.println("Numero de Linhas de nós no arquivo: " + arquivos
												+ " difere do trailer");
										ArquivoCerto = false;
									}
								} else {
									System.out.println("Numero de Linhas de conexoes no arquivo: " + arquivos
											+ " difere do trailer");
									ArquivoCerto = false;
								}
							} else {
								System.out.println("Valor informado do peso no Header e peso somado do arquivo: "
										+ arquivos + " esta diferente");
								ArquivoCerto = false;
							}
						} else {
							System.out.println("Valor informado de Nos do Header e os nos do arquivo: " + arquivos
									+ " esta diferente");
							ArquivoCerto = false;
						}
					}

					br.close();
					MoverArq(arquivos, arquivoDiretorio, ArquivoCerto);

				} else {
					System.out.println("Não é um arquivo ROTA. Arquivo/Diretório encontrado: " + arquivos);
				}

			}

			finally {
				NN = 0;
				somapeso = 0;
				nostrailer = 0;
				conexoestrailer = 0;
				contsomapeso = 0;
				contlinha2 = 0;
				contlinha3 = 0;
				contaNos.clear();
				temHeader = false;
				ArquivoCerto = true;
			}
		}

	}

	/*------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	public static boolean converterHeader(String linha) {
		int nn, somapeso;

		try {
			nn = Integer.parseInt(linha.substring(2, 4));
			somapeso = Integer.parseInt(linha.substring(4, 6));
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/*------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	public void MoverArq(String arq, File arquivo, boolean CertoErrado) throws IOException {
		File caminhoprocessado = new File("Processado");
		File caminhonaoprocessado = new File("NaoProcessado");

		if (CertoErrado) {
			File newcaminho = new File(arquivo + "\\" + arq);
			newcaminho.renameTo(new File(arquivo + "\\" + caminhoprocessado + "\\" + arq));
			System.out.println("Arquivo movido para pasta processado");
		} else {
			File newcaminho = new File(arquivo + "\\" + arq);
			newcaminho.renameTo(new File(arquivo + "\\" + caminhonaoprocessado + "\\" + arq));
			System.out.println("Arquivo movido para pasta Não processado");
		}

	}

	/*------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	public static boolean verificalinha2(String linha, boolean ArquivoCerto) throws IOException {
		String linhas = linha;
		return linhas.contains("=");
	}

	/*------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	public ArrayList<String> verificaNos(String linha, ArrayList<String> listaNos) {
		// contar quantos Nós tem [A, B, C e D]
		String[] lista = new String[2];
		linha = linha.substring(2);
		lista = linha.split("=");

		for (int i = 0; i < lista.length; i++) {
			if (!listaNos.contains(lista[i])) {
				listaNos.add(lista[i]);
			}
		}
		return listaNos;
	}

	/*------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	public static int verificasomalinha3(String linha) {
		int conta = Integer.parseInt(linha.substring(6));
		return conta;
	}

	/*------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	public static int verificatrailerconexao(String linha) {
		int valorconexoes = Integer.parseInt(linha.substring(2, 4));
		return valorconexoes;
	}

	/*------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	public static int verificatrailerpesos(String linha) {
		int valorpesos = Integer.parseInt(linha.substring(2, 4));
		return valorpesos;
	}

}