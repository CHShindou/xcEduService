package com.xuecheng.framework.domain.course;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @Auther : shindou
 * @Date : 2019/4/21
 * @Description : 功能描述
 * @Version : 1.0
 */

@Data
@ToString
@Entity
@NoArgsConstructor
@Table(name="teachplan_media_pub")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class TeachplanMediaPub implements Serializable{

    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(name="teachplan_id")
    private String teachplanId;

    @Column(name="media_id")
    private String mediaId;

    @Column(name="media_fileoriginalname")
    private String mediaFileOriginalName;

    @Column(name="media_url")
    private String mediaUrl;

    @Column(name = "courseid")
    private String courseId;

    @Column(name = "timestamp")
    private Date timestamp;
}
