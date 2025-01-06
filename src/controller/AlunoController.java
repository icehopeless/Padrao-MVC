package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;

import model.Aluno;
import view.AlunoComponent;
import view.JanelaPrincipal;

public class AlunoController {

	/**
	 * @author Raphael
	 */
	
	private JanelaPrincipal view;
	private Aluno model;

	public AlunoController(Aluno model, JanelaPrincipal view) {
		this.view = view;
		this.model = model;
		
		setListeners();
	}
	
	// Adiciona escutadores para todos os componentes das views necessarios
	public void setListeners() {
		
		// Listener para o botao adicionar aluno	
		view.addListenerAddButton(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					if(validate()){
						salvarAluno();
						loadAlunos();
						view.clear();
						view.showMessage("Aluno Salvo!");
					}else{
						view.showMessage("Matricula Existente!");
					}
				} catch (Exception e) {
					
				}
			}
		});

		// Lista com todos os AlunoComponents da view
		ArrayList<AlunoComponent> lista = view.getComponentList();
		
		// Listener para atualizar os dados do aluno
		for (int aux = 0; aux < lista.size(); aux++) {
			
			final int i = aux;
			lista.get(i).addListenerUpdateButton(new ActionListener() {
				private int id;

				@Override
				public void actionPerformed(ActionEvent e) {	
					if(lista.get(i).getButtonEditar().getText().equals("Editar")){
						this.id = lista.get(i).getMatricula();

						lista.get(i).getButtonEditar().setText("Salvar");
						lista.get(i).setBackground(Color.CYAN);
						
						//Ativa o Formulario do componente
						lista.get(i).setEnableComponents(true);
					}else if(lista.get(i).getButtonEditar().getText().equals("Salvar")){
						lista.get(i).getButtonEditar().setText("Editar");
						lista.get(i).setBackground(lista.get(i).getParent().getBackground());

						//atualiza as infos do aluno na base de dados
						updateAluno(id, lista.get(i).getMatricula(), lista.get(i).getName());

						//desativa o formulario do componente
						lista.get(i).setEnableComponents(false);
					}
				}
			});
		}

		// Listener para deletar um aluno
		for (int aux = 0; aux < lista.size(); aux++) {

			final int i = aux;
			lista.get(i).addListenerDeleteButton(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					view.showMessage("Aluno "+ lista.get(i).getName()+"Removido!");
					deleteAluno(lista.get(i).getMatricula());
				}
			});
		}
	
	}

	// Verifica se os dados de matr�cula e nome do aluno s�o v�lidos
	public boolean validate() {

		// Pega os dados da view
		int matricula = view.getMatricula();
		String nome = view.getNameAluno();

		// Verifica se a matricula � v�lida
		ArrayList<Aluno> alunos = model.getAlunos();
		for (int i = 0; i < alunos.size(); i++) {
			if (alunos.get(i).getMatricula() == matricula) {
				return false;
			}
		}

		return true;
	}

	public void salvarAluno() {
		int matricula = view.getMatricula();
		String name = view.getName();
		model.addAluno(matricula, name);
	}

	// Atualiza o modelo de acordo com os dados da view
	public void updateAluno(int id, int matricula, String nome) {
		ArrayList<Aluno> alunos = model.getAlunos();
		for (int i = 0; i < alunos.size(); i++) {
			if (alunos.get(i).getMatricula() == id) {
                alunos.get(i).setMatricula(matricula);
				alunos.get(i).setNome(nome);
				loadAlunos();
				return;
            }
		}
	}

	// Deleta o aluno na base de dados
	public void deleteAluno(int matricula) {
		ArrayList<Aluno> alunos = model.getAlunos();
		for (int i = 0; i < alunos.size(); i++) {
			if (alunos.get(i).getMatricula() == matricula) {
                alunos.remove(i);
                loadAlunos();
				return;
            }
		}
	}
	
	// Atualiza a view com os dados do modelo
	private void loadAlunos() {
		// Recria toda a lista de alunos do painel
		view.getPanelAlunos().removeAll();
		view.getPanelAlunos().repaint();
		
		ArrayList<AlunoComponent> lista = view.getComponentList();
		
		// Cabe�alho 
		JLabel labelMatricula = new JLabel("Matricula");
		labelMatricula.setPreferredSize(new Dimension(80, 20));
		view.getPanelAlunos().add(labelMatricula);
		
		JLabel labelNome = new JLabel("Nome");
		labelNome.setPreferredSize(new Dimension(380, 20));
		view.getPanelAlunos().add(labelNome);
		
		// Acessa as informa��es do modelo
		ArrayList<Aluno> alunos = model.getAlunos();
		lista.clear();
		
		// Cria um AlunoComponent para cada aluno da lista
		for(int i=0;i<alunos.size();i++) {
			lista.add(new AlunoComponent(alunos.get(i).getMatricula()+"", alunos.get(i).getNome()));
		}
		
		for(int i=0;i<lista.size();i++) {
			view.getPanelAlunos().add(lista.get(i));
		}
		
		// Atualiza o tamanho do painel de acordo com o n�mero de alunos
		view.getPanelAlunos().setPreferredSize(new Dimension(200, alunos.size()*50));
		
		view.repaint();
		view.validate();
		
		// Adiciona um escutador para cada bot�o das views
		setListeners();
	}

}
