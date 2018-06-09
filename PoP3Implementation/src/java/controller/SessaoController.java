package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.Mensagem;
import pop3.POP3Session;


@ManagedBean
@SessionScoped
public class SessaoController {

    List<Mensagem> msgs = new ArrayList<>();
    FacesContext facesContext = null;
    HttpSession session = null;
    POP3Session client = null;
    String user = "";
    String password = "";
    String host = "";
    String welcome = "???";
    private Mensagem msgSelecionada;

    public Mensagem getMsgSelecionada() {
        return msgSelecionada;
    }

    public void setMsgSelecionada(Mensagem msgSelecionada) {
        this.msgSelecionada = msgSelecionada;
    }

    public void pegarMsgs() {
        if (facesContext == null || session == null || client == null) {
            client = new POP3Session(host, user, password);
            try {
                client.connectAndAuthenticate();
                session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            } catch (IOException ex) {
                adicionarMensagem("Erro de conexão com o servidor", ex.getMessage(), FacesMessage.SEVERITY_WARN);
            }
        }
        try {
            String[] mensagens = client.getHeaders();
            for (int i = 0; i < mensagens.length; i++) {
                StringTokenizer messageTokens = new StringTokenizer(mensagens[i]);
                Mensagem mensagem = new Mensagem();
                mensagem.setId(i + 1);
                String messageId = messageTokens.nextToken();
                mensagem.setMsg(client.getMessage(messageId));
                mensagem.tratarMsg();
                if (!msgs.contains(mensagem)) {
                    msgs.add(mensagem);
                }
            }
        } catch (Exception ex) {
            adicionarMensagem("Erro ao Receber Mensagens", ex.getMessage(), FacesMessage.SEVERITY_WARN);
        }
    }

    public void start() {
        try {
            if (session == null) {
                facesContext = FacesContext.getCurrentInstance();
                session = (HttpSession) facesContext.getExternalContext().getSession(true);
                client = (POP3Session) session.getAttribute("pop3");
            }
            user = (String) session.getAttribute("user");
            password = (String) session.getAttribute("pw");
            host = (String) session.getAttribute("host");
            welcome = "Bem vindo " + user + ", você tem (" + client.getMessageCount() + ") mensagen(s)";
        } catch (IOException ex) {
            adicionarMensagem("Erro", ex.getMessage(), FacesMessage.SEVERITY_WARN);
        }
    }

    public String exit() {
        session.setAttribute("logado", false);
        client.close();
        session.invalidate();
        return "/security/login.jsf";
    }

    public POP3Session getClient() {
        return client;
    }

    public void setClient(POP3Session client) {
        this.client = client;
    }

    public String getWelcome() {
        return welcome;
    }

    public List<Mensagem> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<Mensagem> msgs) {
        this.msgs = msgs;
    }

    public FacesContext getFacesContext() {
        return facesContext;
    }

    public void setFacesContext(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public void adicionarMensagem(String sumario, String detalhe, FacesMessage.Severity tipoErro) {
        FacesContext contex = FacesContext.getCurrentInstance();
        FacesMessage mensage = new FacesMessage(tipoErro, sumario, detalhe);
        contex.addMessage(null, mensage);

    }

}
