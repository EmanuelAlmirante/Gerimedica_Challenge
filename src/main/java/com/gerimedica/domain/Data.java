package com.gerimedica.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data")
public class Data {
    @Id
    @NotNull
    @Column(name = "code")
    private String code;

    @Column(name = "source")
    private String source;

    @Column(name = "code_list_code")
    private String codeListCode;

    @Column(name = "display_value")
    private String displayValue;

    @NotNull
    @Column(name = "long_description")
    private String longDescription;

    @Basic
    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate;

    @Basic
    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date toDate;

    @Column(name = "sorting_priority")
    private Integer sortingPriority;
}
