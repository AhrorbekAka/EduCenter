package ecma.demo.educenter.entity.attachment;

import ecma.demo.educenter.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image_table")
public class ImageModel extends AbsEntity {
    private String name;
    private String type;

    @Column(name="pic_byte", length = 1000)
    private byte[] picByte;
}
