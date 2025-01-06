import controller.AlunoController;
import model.Aluno;
import view.JanelaPrincipal;

public class TesteMVC {
	
	/**
	 * @author Raphael
	 */
	
	public static void main(String[] args) {
		// modelo
		Aluno model = new Aluno();
		// vis�o
		JanelaPrincipal view = new JanelaPrincipal();
		// controlador
		AlunoController controller = new AlunoController(model, view);
	}
}
