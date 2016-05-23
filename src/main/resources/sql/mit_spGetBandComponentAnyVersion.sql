CREATE PROCEDURE `mit_spGetBandComponentAnyVersion` (
  bandId bigint,
  userId bigint
)
BEGIN

  select bc.*
  from mit_bandcomponent bc
  where bc.bandId = bandId and bc.userId = userId

  UNION

  select bc.*
  from mit_bandcomponent bc
    inner join mit_band b on bc.bandId = b.id
  where b.publishedBandId = bandId and bc.userId = userId;

END
