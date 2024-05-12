package com.fit.fit_be.domain.board.model;

import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.dto.response.BoardResponse;
import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
import com.fit.fit_be.domain.image.model.Image;
import com.fit.fit_be.domain.like.model.Likes;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@OptimisticLocking(type = OptimisticLockType.VERSION)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board", indexes = {
        @Index(name = "idx_created_date_ranking", columnList = "created_date, ranking"),
        @Index(name = "idx_weather_road_condition_place_low_tem_high_tem", columnList = "weather, road_condition, place, lowest_temperature, highest_temperature")
})
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;

    private Long lowestTemperature;

    private Long highestTemperature;

    private boolean open;

    private boolean ranking;

    private int likeCount;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private Weather weather;

    @Enumerated(EnumType.STRING)
    private RoadCondition roadCondition;

    @Enumerated(EnumType.STRING)
    private Place place;

    @CreatedDate
    private LocalDate createdDate;

    @OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<BoardCloth> boardCloths = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Likes> likes = new ArrayList<>();

    @Builder
    public Board(Long id, Member member, String content, Long lowestTemperature, Long highestTemperature, boolean open, boolean ranking, Weather weather, RoadCondition roadCondition, Place place) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.open = open;
        this.ranking = ranking;
        this.weather = weather;
        this.roadCondition = roadCondition;
        this.place = place;
        this.likeCount = 0;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public void addBoardCloth(BoardCloth boardCloth) {
        boardCloths.add(boardCloth);
    }

    public void addLike(Likes like) {
        likes.add(like);
    }

    public BoardResponse toBoardResponse(Long memberId) {
        return BoardResponse.builder()
                .id(id)
                .content(content)
                .lowestTemperature(lowestTemperature)
                .highestTemperature(highestTemperature)
                .open(open)
                .weather(weather)
                .roadCondition(roadCondition)
                .place(place)
                .clothResponses(boardCloths.stream()
                        .map(bd -> bd.getCloth().toClothResponse())
                        .collect(Collectors.toList()))
                .imageUrls(images.stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .like(likes.stream().anyMatch(like -> like.getMember().getId().equals(memberId)))
                .nickname(member.getNickname())
                .isMine(Objects.equals(member.getId(), memberId))
                .build();
    }

    public void updateBoard(UpdateBoardRequest updateBoardRequest) {
        this.content = Objects.requireNonNullElse(updateBoardRequest.getContent(), this.content);
        this.lowestTemperature = Objects.requireNonNullElse(updateBoardRequest.getLowestTemperature(), this.lowestTemperature);
        this.highestTemperature = Objects.requireNonNullElse(updateBoardRequest.getHighestTemperature(), this.highestTemperature);
        this.open = Objects.requireNonNullElse(updateBoardRequest.getOpen(), this.open);
        this.weather = Objects.requireNonNullElse(updateBoardRequest.getWeather(), this.weather);
        this.roadCondition = Objects.requireNonNullElse(updateBoardRequest.getRoadCondition(), this.roadCondition);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
