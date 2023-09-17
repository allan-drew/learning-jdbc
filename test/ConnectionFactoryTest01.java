package jdbc.test;

import jdbc.conexao.ConnectionFactory;
import jdbc.dominio.Alunos;
import jdbc.repository.AlunosRepository;
import jdbc.service.AlunosService;

import java.util.List;


public class ConnectionFactoryTest01 {

    public static void main(String[] args) {

        //------------------------- SAVE (CREATE) -------------------------- //
        // Alunos aluno01 = Alunos.AlunosBuilder.builder().nome("Rapha").build();

        // AlunosRepository.save(aluno01); // acessando pelo Repository
        // AlunosService.save(aluno01); // acessando pelo Service (camada controller)
        //--------------------------------------------------------------------------- //


        //------------------------- DELETE -------------------------- //
        // Para deletar, só precisamos do id, logo, não precisamos criar um aluno para isto:

        // AlunosService.delete(9); // deletado!!
        // AlunosService.delete(8); // deletado!!
        // AlunosService.delete(7); // deletado!!
        // AlunosService.delete(14); // deletado!!
        // AlunosService.delete(15); // deletado!!
        // AlunosService.delete(16); // deletado!!
        //--------------------------------------------------------------------------- //


        //------------------------- UPDATE -------------------------- //
        // Para fazer o update, primeiro criamos um objeto que terá nome e id.
        Alunos alunoParaUpdate = Alunos.AlunosBuilder.builder().nome("Bruno D.").id(6).build();
                                                // Com isso, aluno.getNome() e aluno.getId() no AlunosRepository
                                                // vão passar o nome e o id para o comando sql
        // o nome será usado no SET nome
        // o id será usado na cláusula WHERE

        // AlunosService.update(alunoParaUpdate);
        //--------------------------------------------------------------------------- //


        //------------------------- BUSCAR (READ) -------------------------- //
        // Buscando pelo nome:
        // List<Alunos> listaDeAlunos = AlunosService.buscarPeloNome("Raph");
        // System.out.println(listaDeAlunos);


        // Buscando todos os registros:
        // List<Alunos> todosOsRegistros = AlunosService.buscarTodosOsRegistros();
        // System.out.println(todosOsRegistros);

        //--------------------------------------------------------------------------- //


        //------------------------- SQL Injection test -------------------------- //
        // A injeção de SQL refere-se a uma técnica de exploração de vulnerabilidades de banco de dados.
        // Ocorre quando uma aplicação não valida corretamente as entradas do usuário antes de passá-las para o banco de dados.

        //List<Alunos> listaDeAlunosSqlInj = AlunosService.buscarPeloNomeTesteSqlInjection("Sa"); // sem fazer a injeção de SQL
        //System.out.println(listaDeAlunosSqlInj);


        // Na injeção de SQL, um atacante pode inserir comandos SQL maliciosos na entrada do usuário, explorando a aplicação.
        // A condição X=X é sempre verdadeira, assim, todos os alunos são retornados, pois retorna true para todos os registros!

        //List<Alunos> listaDeAlunosSqlInj02 = AlunosService.buscarPeloNomeTesteSqlInjection("X'='X"); // fazendo a injeção de SQL
        //System.out.println(listaDeAlunosSqlInj02);


        // Para prevenir a injeção de SQL com condição verdadeira, é crucial seguir boas práticas de programação e segurança como usar
        // Parâmetros Preparados (Prepared Statements)!

        // List<Alunos> listaDeAlunosP = AlunosService.buscarPeloNomePreparedStatement("X'='X"); // tentando injetar SQL, porém não foi possível!! PreparedStatement protegeu!
        // System.out.println(listaDeAlunosP);

        // List<Alunos> list = AlunosService.buscarPeloNomePreparedStatement("Vini");
        // System.out.println(list);


        //--------------------------------------------------------------------------- //


        //------------------------- UPDATE (usando PreparedStatement) -------------------------- //
        //Alunos alunoParaUpdatePreparedStatement = Alunos.AlunosBuilder.builder().nome("BRUNO D.").id(6).build(); //
        //AlunosService.updatePreparedStatement(alunoParaUpdatePreparedStatement);
        //--------------------------------------------------------------------------- //



        //------------------------- SAVE (usando Transação) -------------------------- //
        Alunos alunoSaveT00 = Alunos.AlunosBuilder.builder().nome("Thay").build();

        Alunos alunoSaveT01 = Alunos.AlunosBuilder.builder().nome("Leticia").build();
                // Como temos uma exceção ao tentarmos salvar a Leticia,
                // então, nenhuma das tres alunas será adicionada do banco de dados,
                // por causa do ROLLBACK

        Alunos alunoSaveT02 = Alunos.AlunosBuilder.builder().nome("Amanda").build();

        AlunosService.saveTransaction(List.of(alunoSaveT00, alunoSaveT01, alunoSaveT02));
        //--------------------------------------------------------------------------- //



    }


}
