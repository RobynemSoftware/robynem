CREATE DEFINER =`root`@`localhost` PROCEDURE `mit_spPublishClub`(
  stageClubId     BIGINT,
  publishedClubId BIGINT,
  publishUserId   BIGINT
)
  BEGIN
    DECLARE publishedStateId BIGINT;
    SET publishedStateId = (SELECT id
                            FROM mit_entityStatus
                            WHERE code = 'PUBLISHED');

    -- updates published club data
    UPDATE mit_club pub
      INNER JOIN mit_club stage ON pub.id = stage.publishedClubId
    SET pub.description    = stage.description,
      pub.created          = stage.created,
      pub.createdByUserId  = stage.createdByUserId,
      pub.modifiedByUserId = publishUserId,
      pub.name             = stage.name,
      pub.placeId          = stage.placeId,
      pub.addressPlaceId   = stage.addressPlaceId,
      pub.address          = stage.address,
      pub.town             = stage.town,
      pub.website          = stage.website,
      pub.updated          = now(),
      pub.clubLogo         = stage.clubLogo,
      pub.statusId         = publishedStateId,
      pub.firstPublishDate = CASE WHEN pub.firstPublishDate IS NULL
        THEN now()
                             ELSE pub.firstPublishDate END,
      pub.publishedClubId  = NULL
    WHERE stage.id = stageClubId;

    -- update published club owners
    DELETE FROM mit_clubOwnership
    WHERE clubId = publishedClubId;

    INSERT INTO mit_clubOwnership (created, clubId, userId, ownerTypeId)
      SELECT
        now(),
        publishedClubId,
        userId,
        ownerTypeId
      FROM mit_clubOwnership
      WHERE clubId = stageClubId;

    -- delete stage club owners
    DELETE FROM mit_clubOwnership
    WHERE clubId = stageClubId;

    -- update published club genres
    DELETE FROM mit_club_clubgenre
    WHERE clubId = publishedClubId;

    INSERT INTO mit_club_clubgenre (clubId, clubGenreId)
      SELECT
        publishedClubId,
        clubGenreId
      FROM mit_club_clubgenre
      WHERE clubId = stageClubId;

    -- delete stage music genres
    DELETE FROM mit_club_clubgenre
    WHERE clubId = stageClubId;


    -- update published club contacts
    DELETE FROM mit_clubcontact
    WHERE clubId = publishedClubId;

    INSERT INTO mit_clubcontact (created, emailAddress, phoneNumber, clubId)
      SELECT
        created,
        emailAddress,
        phoneNumber,
        publishedClubId
      FROM mit_clubcontact
      WHERE clubId = stageClubId;

    -- delete stage club contacts
    DELETE FROM mit_clubcontact
    WHERE clubId = stageClubId;

    -- update published opening info
    DELETE FROM mit_clubopeninginfo
    WHERE clubId = publishedClubId;

    INSERT INTO mit_clubopeninginfo (created, updated, endDay, endHour, opened, startDay, startHour, clubId)
      SELECT
        created,
        updated,
        endDay,
        endHour,
        opened,
        startDay,
        startHour,
        publishedClubId
      FROM mit_clubopeninginfo
      WHERE clubId = stageClubId;

    DELETE from mit_clubopeninginfo
    where clubId = stageClubId;

    -- update published club images
    CREATE TEMPORARY TABLE imagesToDelete
      ENGINE = MEMORY
        SELECT imageId
        FROM mit_clubimage
        WHERE clubId = publishedClubId;

    DELETE FROM mit_clubimage
    WHERE clubId = publishedClubId;

    INSERT INTO mit_clubimage (clubId, imageId)
      SELECT
        publishedClubId,
        imageId
      FROM mit_clubimage
      WHERE clubId = stageClubId;

    -- delete stage band images
    DELETE FROM mit_clubimage
    WHERE clubId = stageClubId;

    -- Deletes old published images
    DELETE i
    FROM mit_image i
      INNER JOIN imagesToDelete tmp ON i.id = tmp.imageId;

    DROP TEMPORARY TABLE IF EXISTS imagesToDelete;

    -- delete stage band 
    DELETE FROM mit_club
    WHERE id = stageClubId;

  END