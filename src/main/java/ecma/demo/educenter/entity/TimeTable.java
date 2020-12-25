package ecma.demo.educenter.entity;

import ecma.demo.educenter.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Month;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TimeTable extends AbsEntity {

    @Column(nullable=false)
    private Integer month;

    private double paymentForThisMonth;

//    @Enumerated(EnumType.STRING)
//    private WeekDays weekDays;
}
