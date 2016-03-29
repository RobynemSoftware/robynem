package com.robynem.mit.web.controller.home;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.home.NewArtistModel;
import com.robynem.mit.web.model.home.NewClubsModel;
import com.robynem.mit.web.model.home.NextEventModel;
import com.robynem.mit.web.persistence.criteria.NewArtistsCriteria;
import com.robynem.mit.web.persistence.dao.HomeDao;
import com.robynem.mit.web.persistence.util.ArtistMapResult;
import com.robynem.mit.web.util.ImageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by roberto on 06/12/2015.
 */
@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomeDao homeDao;

    @RequestMapping(method = RequestMethod.GET)
    public String viewIndex() {
        return "index";
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String viewIndex2() {
        return "index";
    }

    @RequestMapping("favicon.ico")
    public String favicon() {
        return "forward:/resources/images/favicon.ico";
    }

    @RequestMapping("home/loadNextEvents")
     public ModelAndView loadNextEvents() {
        ModelAndView mv = new ModelAndView("home/nextEventsCarousel");
        mv.addObject("nextEvents", this.stubNextEventsList()) ;
        return mv;
    }

    @RequestMapping("home/loadNewArtists")
    public ModelAndView loadNewArtists(ModelMap modelMap) {
        ModelAndView mv = new ModelAndView("home/newArtistsCarousel");

        try {
            mv.addObject("newArtists", this.getNewArtistsList()) ;
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return mv;
    }

    @RequestMapping("home/loadNewClubs")
    public ModelAndView loadNewClubs() {
        ModelAndView mv = new ModelAndView("home/newClubsCarousel");
        mv.addObject("newClubs", this.stubNewClubsList()) ;
        return mv;
    }

    private List<NextEventModel> stubNextEventsList() {
        List<NextEventModel> list = new ArrayList<NextEventModel>();

        NextEventModel model = null;

        model = new NextEventModel();
        model.setArtistDescription("Brit Rock");
        model.setArtistName("Britpop");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/britpop.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(false);
        model.setPhone(false);
        model.setSpecialOffer(false);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Psychedelic");
        model.setArtistName("BUC");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/buc.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(true);
        model.setPhone(false);
        model.setSpecialOffer(false);

        list.add(model);


        model = new NextEventModel();
        model.setArtistDescription("Funky");
        model.setArtistName("Capatosta");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/capatosta.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(true);
        model.setPhone(true);
        model.setSpecialOffer(true);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Pop Rock");
        model.setArtistName("Freaky Trip");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/freaky-trip.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(false);
        model.setPhone(true);
        model.setSpecialOffer(true);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Pearl Jam Tribute");
        model.setArtistName("Given To Fly");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/given-to-fly.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(false);
        model.setPhone(false);
        model.setSpecialOffer(true);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Metal");
        model.setArtistName("Metharia");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/metharia.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(true);
        model.setPhone(false);
        model.setSpecialOffer(true);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Pop Rock");
        model.setArtistName("Moonflower");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/moonflowers.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(false);
        model.setPhone(false);
        model.setSpecialOffer(false);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Litfiba Tribute");
        model.setArtistName("Morgana");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/morgana.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(false);
        model.setPhone(true);
        model.setSpecialOffer(true);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Subsonica Tribute");
        model.setArtistName("Non Identificato");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/non-identificato.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(true);
        model.setPhone(true);
        model.setSpecialOffer(true);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Pink Floyd Tribute");
        model.setArtistName("Pigs On The Moon");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/pigs-on-the-moon.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(false);
        model.setPhone(false);
        model.setSpecialOffer(false);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Rock");
        model.setArtistName("Raul And Reloaded");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/raul-and-reloaded.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(false);
        model.setPhone(true);
        model.setSpecialOffer(false);

        list.add(model);

        model = new NextEventModel();
        model.setArtistDescription("Rock");
        model.setArtistName("Sistema");
        model.setEventDetails("Napoli - 08/12/2015 - 22:30");
        model.setEventPlaceType("Pub / Birreria");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/sistema.jpg");
        model.setEventPlaceName("Goodfellas");
        model.setMoney(true);
        model.setPhone(false);
        model.setSpecialOffer(true);

        list.add(model);



        return list;
    }

    private List<NewArtistModel> getNewArtistsList() {
        List<NewArtistModel> list = new ArrayList<NewArtistModel>();

        NewArtistModel model = null;

        ArtistMapResult mapResult = this.homeDao.getNewArtists(new NewArtistsCriteria());

        while (mapResult.next()) {
            model = new NewArtistModel();
            model.setId(mapResult.get(ArtistMapResult.ID));
            model.setArtistDescription(mapResult.get(ArtistMapResult.ARTIST_DESCRIPTION));
            model.setArtistName(mapResult.get(ArtistMapResult.NAME));

            String imageUrl = this.getContextPath() + "/resources/images/profile_avatar_50x50.png";

            if (mapResult.get(ArtistMapResult.IMAGE_ID) != null) {
                imageUrl = this.getContextPath() + String.format("/media/getImage?imageId=%s&size=%s", mapResult.get(ArtistMapResult.IMAGE_ID).toString(), ImageSize.MEDIUM);
            }

            model.setImgUrl(imageUrl);

            model.setFirstPublishDate(mapResult.get(ArtistMapResult.FIRST_PUBLISH_DATE));

            model.setArtistType(mapResult.get(ArtistMapResult.ARTIST_TYPE).toString());

            list.add(model);
        }

        return list.stream().sorted((m1, m2) -> m2.getFirstPublishDate().compareTo(m1.getFirstPublishDate())).collect(Collectors.toList());
    }

    private List<NewArtistModel> stubNewArtistsList() {
        List<NewArtistModel> list = new ArrayList<NewArtistModel>();

        NewArtistModel model = null;

        model = new NewArtistModel();
        model.setArtistDescription("Brit Rock");
        model.setArtistName("Britpop");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/britpop.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Psychedelic");
        model.setArtistName("BUC");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/buc.jpg");

        list.add(model);


        model = new NewArtistModel();
        model.setArtistDescription("Funky");
        model.setArtistName("Capatosta");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/capatosta.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Pop Rock");
        model.setArtistName("Freaky Trip");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/freaky-trip.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Pearl Jam Tribute");
        model.setArtistName("Given To Fly");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/given-to-fly.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Metal");
        model.setArtistName("Metharia");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/metharia.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Pop Rock");
        model.setArtistName("Moonflower");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/moonflowers.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Litfiba Tribute");
        model.setArtistName("Morgana");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/morgana.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Subsonica Tribute");
        model.setArtistName("Non Identificato");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/non-identificato.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Pink Floyd Tribute");
        model.setArtistName("Pigs On The Moon");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/pigs-on-the-moon.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Rock");
        model.setArtistName("Raul And Reloaded");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/raul-and-reloaded.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Rock");
        model.setArtistName("Sistema");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/sistema.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Pink Floyd Tribute");
        model.setArtistName("Pigs On The Moon");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/pigs-on-the-moon.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Pop Rock");
        model.setArtistName("Freaky Trip");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/freaky-trip.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Brit Rock");
        model.setArtistName("Britpop");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/britpop.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Rock");
        model.setArtistName("Sistema");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/sistema.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Subsonica Tribute");
        model.setArtistName("Non Identificato");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/non-identificato.jpg");

        list.add(model);

        model = new NewArtistModel();
        model.setArtistDescription("Pink Floyd Tribute");
        model.setArtistName("Pigs On The Moon");
        model.setImgUrl(this.getContextPath() + "/resources/demo-bands/pigs-on-the-moon.jpg");

        list.add(model);

        return list;
    }

    private List<NewClubsModel> stubNewClubsList() {
        List<NewClubsModel> list = new ArrayList<NewClubsModel>();

        NewClubsModel model = null;

        model = new NewClubsModel();
        model.setClubName("Billy Bob's");
        model.setImgUrl(this.getContextPath() + "/resources/demo-clubs/billy-bobs.jpg");
        model.setClubType("Pub | Birreria");

        list.add(model);

        model = new NewClubsModel();
        model.setClubName("Good Fellas");
        model.setImgUrl(this.getContextPath() + "/resources/demo-clubs/GoodFellas.jpg");
        model.setClubType("Pub | Birreria");

        list.add(model);

        model = new NewClubsModel();
        model.setClubName("Hades");
        model.setImgUrl(this.getContextPath() + "/resources/demo-clubs/hades.jpeg");
        model.setClubType("Pub | Birreria");

        list.add(model);

        model = new NewClubsModel();
        model.setClubName("45");
        model.setImgUrl(this.getContextPath() + "/resources/demo-clubs/45.png");
        model.setClubType("Pub | Birreria");

        list.add(model);

        model = new NewClubsModel();
        model.setClubName("New Sea Legend");
        model.setImgUrl(this.getContextPath() + "/resources/demo-clubs/sea-legend.jpg");
        model.setClubType("Pub | Birreria");

        list.add(model);

        model = new NewClubsModel();
        model.setClubName("Tenax");
        model.setImgUrl(this.getContextPath() + "/resources/demo-clubs/tenax.jpg");
        model.setClubType("Pub | Birreria");

        list.add(model);

        return list;
    }
}
