package ca.com.rlsp.ecommerce.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/*
Essa classe terá Tem 2 FUNCOES:
 1) Criar Autenticacao JWT
 2) Retornar a Autenticao JWT
 */

@Service
public class JWTTokenAuthenticationService{

    /* Tempo de validade do Token  (30 dias) */
    private static final Long EXPIRATINN_TIME = 2592000000L;

    /* Chave de seguranca para ser usada na criptografia (Md5, base64 ,etc)*/
    private static final String SECRET = "ZWNvbW1lcmNl";

    /* Prefixo do TOKEN (Bearer ,API key )*/
    private static final String TOKEN_PREFIX = "Bearer";

    /* Prefixo do Cabecalho de Retorno do pedido de autenticacao (Header) */
    private static final String HEADER_STRING = "Authorization";

    /* Gera o Token e envia a resposta para o cliente com JWT*/
    /* Recebe o username mas a validacao e manda um retorno para o cliente*/
    public void addAuthentication(HttpServletResponse response, String username) throws Exception{

        /* Montagem do Token*/

        /* JWT part*/
        String JWT = Jwts
                        .builder() /* Chama o gerador de Token */
                        .setSubject(username)  /* Adiciona o user */
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATINN_TIME)) /* Tempo de Expiracao */
                        .signWith(SignatureAlgorithm.HS512, SECRET).compact(); /* Assina e compacta o token */

        /* Agrupa toda informacao do Token */
        /* Ex: Bearer ajdofijasoijfdaiojsdoajsodjaoisjdoaijsdasdoajsd */
        String token = TOKEN_PREFIX + " " + JWT;


        /* Retorna para a TELA e para o CLIENTE (API, browser, app, JS, Java App, etc) */
        /* 1) No Cabecalho*/
        response.addHeader(HEADER_STRING, token);

        /* 2) No corpo da resposta  (USADO no POSTMAN */
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }


}