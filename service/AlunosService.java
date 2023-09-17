package jdbc.service;

import jdbc.dominio.Alunos;
import jdbc.repository.AlunosRepository;

import java.util.List;

// A camada de Service pode ser entendida como a camada Controller.

// Estamos, portanto, adicionando uma camada ao sistena, dividindo responsabilidades.
// Logo, na camada View (Test), não estamos mais acessando diretamente o Repository, e sim acessando pelo controller (service).


public class AlunosService {

    public static void save(Alunos student) {

        // Como o método save de AlunosRepository é static, conseguimos chamá-lo sem precisar de uma instância da classe.
        // Assim ao chamar o save em AlunosService, estamos adicionando uma camada ao sistema, dividindo responsabilidades.
        AlunosRepository.save(student);
    }

    public static void delete(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("valor inválido para o ID");
        }
        AlunosRepository.delete(id);
    }

    public static void update(Alunos student) {

        if (student.getId() == null || student.getId() <= 0) {
            throw new IllegalArgumentException("valor inválido");
        }

        AlunosRepository.update(student);
    }


    public static List<Alunos> buscarPeloNome(String nome) {
        return AlunosRepository.buscarPeloNome(nome);
    }


    public static List<Alunos> buscarTodosOsRegistros() {
        return AlunosRepository.buscarTodosOsRegistros();
    }


    public static List<Alunos> buscarPeloNomeTesteSqlInjection(String nome) {
        return AlunosRepository.buscarPeloNomeTesteSqlInjection(nome);
    }


    public static List<Alunos> buscarPeloNomePreparedStatement(String nome) {
        return AlunosRepository.buscarPeloNomePreparedStatement(nome);
    }


    public static void updatePreparedStatement(Alunos student) {

        if (student.getId() == null || student.getId() <= 0) {
            throw new IllegalArgumentException("valor inválido");
        }

        AlunosRepository.updatePreparedStatement(student);
    }



    public static void saveTransaction(List<Alunos> alunosList) {
        AlunosRepository.saveTransaction(alunosList);
    }


}
