CREATE DEFINER=`root`@`localhost` PROCEDURE `mit_spPublishBand`(
	stageBandId bigint,
    publishedBandId bigint,
    publishUserId bigint
)
BEGIN
	declare publishedStateId bigint;
    set publishedStateId = (select id from mit_entityStatus where code = 'PUBLISHED');

	-- updates published band data
    update mit_band pub 
		inner join mit_band stage on pub.id = stage.publishedBandId
	set pub.biography = stage.biography,
		pub.created = stage.created,
        pub.createdByUserId = stage.createdByUserId,
        pub.modifiedByUserId = publishUserId,
        pub.name = stage.name,
        pub.placeId = stage.placeId,
        pub.town = stage.town,
        pub.website = stage.website,
        pub.updated = curdate(),
        pub.bandLogo = stage.bandLogo,
        pub.statusId = publishedStateId,
        pub.firstPublishDate = case when pub.firstPublishDate is null then now() else pub.firstPublishDate end,
        pub.publishedBandId = null
    where stage.id = stageBandId;
    
    -- update published band owners
    delete from mit_bandOwnership where bandId = publishedBandId;
    
    insert into mit_bandOwnership(created, bandId, userId, ownerTypeId)
		select curdate(), publishedBandId, userId, ownerTypeId
        from mit_bandOwnership
        where bandId = stageBandId;
        
	-- delete stage band owners
    delete from mit_bandOwnership where bandId = stageBandId;
        
	-- update published band music genres
    delete from mit_bandMusicGenre where bandId = publishedBandId;
    
    insert into mit_bandMusicGenre (bandId, musicGenreId)
		select publishedBandId, musicGenreId
        from mit_bandMusicGenre
        where bandId = stageBandId;
        
	-- delete stage music genres
    delete from mit_bandMusicGenre where bandId = stageBandId;
        
	-- update published band components
    delete pci 
    from mit_bandcomponentinstrument pci
		inner join mit_bandcomponent pc on pci.bandComponentId = pc.id
	where pc.bandId = publishedBandId;
    
    delete from mit_bandcomponent where bandId = publishedBandId;
    
    insert into mit_bandcomponent (created, confirmed, bandId, userId, discJockey, singer)
		select curdate(), confirmed, publishedBandId, userId, discJockey, singer
        from mit_bandcomponent
        where bandId = stageBandId;
        
	insert into mit_bandcomponentinstrument (bandComponentId, musicalInstrumentId)
		select pc.id, sci.musicalInstrumentId
        from mit_bandcomponentinstrument sci
			inner join mit_bandcomponent sc on sci.bandComponentId = sc.id and sc.bandId = stageBandId
            inner join mit_bandcomponent pc on sc.userId = pc.userId and pc.bandId = publishedBandId;
            
	-- delete stage band components and instruments
    delete sci 
    from mit_bandcomponentinstrument sci
		inner join mit_bandcomponent sc on sci.bandComponentId = sc.id
	where sc.bandId = stageBandId;
    
    delete from mit_bandcomponent where bandId = stageBandId;
            
	-- update published band contacts
    delete from mit_bandContact where bandId = publishedBandId;
    
    insert into mit_bandContact (created, emailAddress, phoneNumber, bandId)
		select created, emailAddress, phoneNumber, publishedBandId
        from mit_bandContact
        where bandId = stageBandId;
        
	-- delete stage band contacts
    delete from mit_bandContact where bandId = stageBandId;
	
    -- update published band videos
    delete from mit_bandVideo where bandId = publishedBandId;
    
    insert into mit_bandVideo (bandId, videoId)
		select publishedBandId, videoId
        from mit_bandVideo
        where bandId = stageBandId;
        
	-- delete stage band videos
    delete from mit_bandVideo where bandId = stageBandId;
        
	-- update published band images
    delete from mit_bandImage where bandId = publishedBandId;
    
    insert into mit_bandImage(bandId, imageId)
		select publishedBandId, imageId
        from mit_bandImage
        where bandId = stageBandId;
        
	-- delete stage band images
    delete from mit_bandImage where bandId = stageBandId;
        
	-- update published band audios
    delete from mit_bandAudio where bandId = publishedBandId;
    
    insert into mit_bandAudio(bandId, audioId)
		select publishedBandId, audioId
        from mit_bandAudio
        where bandId = stageBandId;
        
	-- delete stage band audios
    delete from mit_bandAudio where bandId = stageBandId;
    
    -- delete stage band 
    delete from mit_band where id = stageBandId;
    
END