package com.robynem.mit.web.persistence.entity;



import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by roberto on 11/12/2015.
 */
@Entity
@Table(name="mit_user")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_USER_BY_EMAIL_ADDRESS", query = "from UserEntity u where u.emailAddress = :emailAddress"),
        @NamedQuery(name = "@HQL_GET_USER_BY_FACEBOOK_ID", query = "from UserEntity u where u.facebookId = :facebookId"),
        @NamedQuery(name = "@HQL_GET_USER_BY_EMAIL_AND_PASSWORD", query = "from UserEntity u where u.emailAddress = :emailAddress and u.password = :password"),
        @NamedQuery(name = "@HQL_FILTER_MISICIANS_FOR_AUTOCOMPLETE", query = "select new UserEntity (u.id, u.firstName, u.lastName, u.profileImage.id) " +
                "from UserEntity  u " +
                "where u.musician = true and (u.firstName like :name or u.lastName like :name)")
})
public class UserEntity extends BaseEntity {

    @Column(nullable=false, length=50)
    protected String firstName;

    @Column(nullable=false, length=50)
    protected String lastName;

    @Column(nullable=false, length=100, unique = true)
    protected String emailAddress;

    @Column(nullable=false, length=50)
    protected String password;

    @Column(nullable = true, columnDefinition = "varchar(10)")
    protected String gender;

    @Column
    protected Date birthDate;

    @Column
    protected String placeId;

    @Column
    protected String town;

    @Column(nullable=true)
    protected Date lastLogin;

    @Column(nullable=true)
    protected Long facebookId;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    protected boolean engagementAvailable;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profileImage")
    protected ImageEntity profileImage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mit_userMusicGenre",
            joinColumns = {@JoinColumn(name = "userId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "musicGenreId", nullable = false) }
    )
    protected Set<MusicGenreEntity> preferredMusicGenres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mit_userMusicalInstrument",
            joinColumns = {@JoinColumn(name = "userId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "musicalInstrumentId", nullable = false) }
    )
    protected Set<MusicalInstrumentEntity> playedMusicInstruments;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    protected boolean singer;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    protected boolean musicMaster;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    protected boolean discJockey;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mit_masterInstrument",
            joinColumns = {@JoinColumn(name = "userId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "musicalInstrumentId", nullable = false) }
    )
    protected Set<MusicalInstrumentEntity> masterInstruments;

    @Column(columnDefinition = "LONGTEXT")
    protected String biography;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    protected boolean musician;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    protected Set<BandOwnershipEntity> ownedBands;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    protected Set<BandComponentEntity> bandsPlaying;

    public UserEntity() {
        this.engagementAvailable = true;
    }

    public UserEntity(Long id, String firstName, String lastName, Long profileImageId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        if (profileImageId != null) {
            this.profileImage = new ImageEntity();
            this.profileImage.setId(profileImageId);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Long facebookId) {
        this.facebookId = facebookId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public ImageEntity getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ImageEntity profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isEngagementAvailable() {
        return engagementAvailable;
    }

    public void setEngagementAvailable(boolean engagementAvailable) {
        this.engagementAvailable = engagementAvailable;
    }

    public Set<MusicGenreEntity> getPreferredMusicGenres() {
        return preferredMusicGenres;
    }

    public void setPreferredMusicGenres(Set<MusicGenreEntity> preferredMusicGenres) {
        this.preferredMusicGenres = preferredMusicGenres;
    }

    public Set<MusicalInstrumentEntity> getPlayedMusicInstruments() {
        return playedMusicInstruments;
    }

    public void setPlayedMusicInstruments(Set<MusicalInstrumentEntity> playedMusicInstruments) {
        this.playedMusicInstruments = playedMusicInstruments;
    }

    public boolean isSinger() {
        return singer;
    }

    public void setSinger(boolean singer) {
        this.singer = singer;
    }

    public boolean isMusicMaster() {
        return musicMaster;
    }

    public void setMusicMaster(boolean musicMaster) {
        this.musicMaster = musicMaster;
    }

    public Set<MusicalInstrumentEntity> getMasterInstruments() {
        return masterInstruments;
    }

    public void setMasterInstruments(Set<MusicalInstrumentEntity> masterInstruments) {
        this.masterInstruments = masterInstruments;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean isMusician() {
        return musician;
    }

    public void setMusician(boolean musician) {
        this.musician = musician;
    }

    public Set<BandOwnershipEntity> getOwnedBands() {
        return ownedBands;
    }

    public void setOwnedBands(Set<BandOwnershipEntity> ownedBands) {
        this.ownedBands = ownedBands;
    }

    public Set<BandComponentEntity> getBandsPlaying() {
        return bandsPlaying;
    }

    public void setBandsPlaying(Set<BandComponentEntity> bandsPlaying) {
        this.bandsPlaying = bandsPlaying;
    }

    public boolean isDiscJockey() {
        return discJockey;
    }

    public void setDiscJockey(boolean discJockey) {
        this.discJockey = discJockey;
    }
}
