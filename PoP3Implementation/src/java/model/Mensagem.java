package model;

import java.util.Objects;


public class Mensagem {

    Integer id;
    String from = "";
    String to = "";
    String subject = "";
    String date = "";
    String body = "";
    String msg = "";
    int i = 0;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mensagem other = (Mensagem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public void imprimeMSGT() {
        System.out.println("Msg");
        System.out.println("ID:" + id);
        System.out.println("FROM:" + from);
        System.out.println("TO:" + to);
        System.out.println("SUBJECT:" + subject);
        System.out.println("DATE:" + date);
        System.out.println("BODY:" + body);
        System.out.println("MSG:" + msg);
    }

    public void tratarMsg() {
        if (!msg.equals("")) {
            for (i = msg.indexOf("From:") + 5; msg.charAt(i) != '\n'; i++) {
                if (msg.charAt(i) != ' ') {
                    from += msg.charAt(i);
                }
            }
            for (i = msg.indexOf("To:") + 3; msg.charAt(i) != '\n'; i++) {
                if (msg.charAt(i) != ' ') {
                    to += msg.charAt(i);
                }
            }
            for (i = msg.indexOf("Subject:") + 8; msg.charAt(i) != '\n'; i++) {
                subject += msg.charAt(i);
            }
            for (i = msg.indexOf("Date:") + 5; msg.charAt(i) != '\n'; i++) {
                date += msg.charAt(i);
            }
            for (; i < msg.length(); i++) {
                body += msg.charAt(i);
            }
        } else {
            System.out.println("NÃO TRATANDO");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
