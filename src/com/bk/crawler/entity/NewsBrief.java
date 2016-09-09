package com.bk.crawler.entity;

import javax.persistence.*;

/**
 * Created by shikee_app03 on 16/7/20.
 */
@Entity(name = "NewsBriefModel")
public class NewsBrief {

    private Long nbId;//新闻简介ID
    private String title;//新闻标题

    public NewsBrief() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getNbId() {
        return nbId;
    }

    public void setNbId(Long nbId) {
        this.nbId = nbId;
    }
    @Column(columnDefinition="TEXT")
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    @Column(nullable=false)
    public Long getnId() {
        return nId;
    }

    public void setnId(Long nId) {
        this.nId = nId;
    }



    private String brief;//新闻摘要
    private String imgUrl;//新闻出现的第一张图片URL
    private String time;//新闻发布时间
    private String source;//新闻来源
    private Long nId;//所属的新闻ID

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id",nullable = false)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    private Category category;
}
