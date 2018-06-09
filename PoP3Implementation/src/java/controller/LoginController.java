package controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.Login;
import pop3.POP3Session;

@ManagedBean
@SessionScoped
public class LoginController {

    Login login = new Login();
    POP3Session client = null;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
    
    public void teste(){
        System.out.println("TESTE");
    }

    public String logar() {
        try {
            client = new POP3Session(login.getHost(), login.getUsuario(), login.getSenha());
            client.connectAndAuthenticate();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("pop3", client);
            session.setAttribute("user", login.getUsuario());
            session.setAttribute("pw", login.getSenha());
            session.setAttribute("host", login.getHost());
            session.setAttribute("logado", true);
            return "/app/index?faces-redirect=true";
             
        } catch (Exception e) {
            adicionarMensagem("Falha no login!", e.getMessage(), FacesMessage.SEVERITY_WARN);
            client.close();
            return "";
        }
    }

    public POP3Session getClient() {
        return client;
    }

    public void setClient(POP3Session client) {
        this.client = client;
    }

    public void adicionarMensagem(String sumario, String detalhe, FacesMessage.Severity tipoErro) {
        FacesContext contex = FacesContext.getCurrentInstance();
        FacesMessage mensage = new FacesMessage(tipoErro, sumario, detalhe);
        contex.addMessage(null, mensage);

    }

}
