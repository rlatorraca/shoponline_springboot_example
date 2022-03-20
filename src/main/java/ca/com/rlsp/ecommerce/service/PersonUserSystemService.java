package ca.com.rlsp.ecommerce.service;

import ca.com.rlsp.ecommerce.model.LegalPerson;
import ca.com.rlsp.ecommerce.model.UserSystem;
import ca.com.rlsp.ecommerce.repository.PersonRepository;
import ca.com.rlsp.ecommerce.repository.UserSystemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PersonUserSystemService {


    private UserSystemRepository userSystemRepository;
    private PersonRepository personRepository;
    private JdbcTemplate jdbcTemplate;

    public PersonUserSystemService(UserSystemRepository userSystemRepository, PersonRepository personRepository, JdbcTemplate jdbcTemplate) {
        this.userSystemRepository = userSystemRepository;
        this.personRepository = personRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public LegalPerson saveLegalPerson(LegalPerson legalPerson) {


        // Salva empresa no BD
        //legalPerson = personRepository.save(legalPerson);

        for (int i = 0; i< legalPerson.getAddresses().size(); i++) {
           legalPerson.getAddresses().get(i).setPerson(legalPerson);
            legalPerson.getAddresses().get(i).setEcommerceCompany(legalPerson);
        }

        legalPerson = personRepository.save(legalPerson);

        // Verifica se existe usuario com mesmo ID and Email
        UserSystem userLegalPerson = userSystemRepository.findUserSystemByPerson(
                                                                legalPerson.getId(),
                                                                legalPerson.getEmail());

        // Se NAO existir o Usuario de Sistema criado
        // Cria um Usuario de Sistema se a Empresa nao tiver usuario criado
        if (userLegalPerson == null) {

            // Verifica se existe uma constraint no BD que nao permite a gravacao de Access_id na tabela de conexao
            //    entra as tabelas User_System e role_access (chamada de user_role_access)
            String constraint_Error_User_Role_Access = userSystemRepository.queryConstraintUserRoleAcessoTable();
            if (constraint_Error_User_Role_Access != null) {
                jdbcTemplate.execute("begin; " +
                                         "alter table user_role_access drop constraint "
                                                + constraint_Error_User_Role_Access +
                                         "; commit;");
            }

            // Cria o novo usuario para Empresa que foi criada e cadastrada
            userLegalPerson = new UserSystem();
            userLegalPerson.setLastPasswordDate(Calendar.getInstance().getTime());
            userLegalPerson.setEcommerceCompany(legalPerson);
            userLegalPerson.setPerson(legalPerson);
            userLegalPerson.setLogin(legalPerson.getEmail());

            String password = "" + Calendar.getInstance().getTimeInMillis();
            String passwordEncoded = new BCryptPasswordEncoder().encode(password);

            userLegalPerson.setPassword(passwordEncoded);

            // Save a UserSystem
            userLegalPerson = userSystemRepository.save(userLegalPerson);

            // Cria um Usuario STANDARD (para acessar o sistema)
            userSystemRepository.insertStandardUserLegalPerson(userLegalPerson.getId());

            /*Fazer o envio de e-mail do login e da senha*/

        }

        return legalPerson;

    }

}
