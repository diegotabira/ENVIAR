package util;

import java.io.IOException;

import exceptions.CrashException;
import terminal.ADBComunicator;
import terminal.Commands;

public class Logcather {
	
	private String logcat;
	
	public Logcather() {
		logcat = "";
	}
	
//	V — Detalhada (prioridade mais baixa)
//	D — Depuração
//	I — Informação
//	W — Aviso
//	E — Erro
//	F — Fatal
//	S — Silenciosa (prioridade mais alta, na qual nada é exibido)
//	private char ajustarPrioridade(char prioridade) {
//		prioridade = Character.toLowerCase(prioridade);
//		if(prioridade != 'v' && prioridade != 'd' && prioridade != 'i' &&
//		   prioridade != 'w' && prioridade != 'e' && prioridade != 'f' &&
//		   prioridade != 's') {
//			prioridade = 'v';
//		}
//		return prioridade;
//	}
	
//	public String getLogcat(char prioridade) throws IOException {
//		prioridade = ajustarPrioridade(prioridade);
//		String cmd = "adb -e logcat " + Util.APP_PKG + ":" + prioridade + " *:S";
//		ADBComunicator adbc = new ADBComunicator();
//		logcat = adbc.runADBCommand(cmd, false);
//		return logcat;
//	}
	
	public void monitorLogcat() throws IOException, CrashException {
		ADBComunicator.getInstance().runADBLogcatCommand();
	}
	
	public String getLogcat() throws IOException, InterruptedException {
		String cmd = Commands.GET_ALL_LOGCAT;
		logcat = ADBComunicator.getInstance().runADBCommand(cmd);
		return logcat;
	}

	public void clear() throws IOException, InterruptedException {
		String cmd = Commands.CLEAR_LOGCAT;
		ADBComunicator.getInstance().runADBCommand(cmd);
	}

}
