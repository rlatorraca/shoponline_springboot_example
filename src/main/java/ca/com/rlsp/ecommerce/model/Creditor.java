package ca.com.rlsp.ecommerce.model;

import ca.com.rlsp.ecommerce.enums.StatusCreditor;
import ca.com.rlsp.ecommerce.enums.StatusDebtor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "creditor")
@SequenceGenerator(name = "seq_creditor", sequenceName = "seq_creditor", initialValue = 1 , allocationSize = 1)
public class Creditor implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_creditor")
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private StatusCreditor statusCreditor;

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Temporal(TemporalType.DATE)
    private Date paymentDate;

    private BigDecimal totalValue;

    private BigDecimal totalDiscount;

    @ManyToOne(targetEntity = Person.class)
    @JoinColumn(name = "person_id",
                nullable = false,
                foreignKey = @ForeignKey(
                        value = ConstraintMode.CONSTRAINT,
                        name = "person_fk"))
    private Person person;

    @ManyToOne(targetEntity = Person.class)
    @JoinColumn(name = "person_provider_id",
                nullable = false,
                foreignKey = @ForeignKey(
                        value = ConstraintMode.CONSTRAINT,
                        name = "person_provider_fk"))
    private Person person_provider;

    public StatusCreditor getStatusCreditor() {
        return statusCreditor;
    }

    public Person getPerson_provider() {
        return person_provider;
    }

    public void setPerson_provider(Person person_provider) {
        this.person_provider = person_provider;
    }

    public void setStatusCreditor(StatusCreditor statusCreditor) {
        this.statusCreditor = statusCreditor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
