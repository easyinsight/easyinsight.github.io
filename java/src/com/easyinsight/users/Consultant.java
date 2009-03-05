package com.easyinsight.users;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Mar 1, 2009
 * Time: 11:58:11 AM
 */
@Entity
@Table(name="guest_user")
public class Consultant {

    public static final int PENDING_EI_APPROVAL = 1;
    public static final int ACTIVE = 2;
    public static final int DISABLED = 3;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long guestUserID;

    @Column(name="state")
    private int state;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getGuestUserID() {
        return guestUserID;
    }

    public void setGuestUserID(Long guestUserID) {
        this.guestUserID = guestUserID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ConsultantTO toConsultantTO() {
        ConsultantTO consultantTO = new ConsultantTO();
        consultantTO.setState(state);
        consultantTO.setUserTransferObject(user.toUserTransferObject());
        consultantTO.setConsultantID(guestUserID);
        return consultantTO;
    }

    public EIConsultant toEIConsultant() {
        EIConsultant eiConsultant = new EIConsultant();
        eiConsultant.setState(state);
        eiConsultant.setUserTransferObject(user.toUserTransferObject());
        eiConsultant.setConsultantID(guestUserID);
        return eiConsultant;
    }
}
