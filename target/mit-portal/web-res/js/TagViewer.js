/**
 * Created by robyn_000 on 07/01/2016.
 */

var TAG_DEFAULT_CLASS = "tag";

function TagViewer(settings) {
    var $this = this;
    var $selectId = settings.selectId;
    var $tagContainerId = settings.tagContainerId;
    var $paramName = settings.paramName;
    var $deleteTooltip = settings.deleteTooltip;
    var $tagClass = settings.tagClass != null ? settings.tagClass : TAG_DEFAULT_CLASS;
    var $tagMobileClass = settings.tagMobileClass != null ? settings.tagMobileClass : $tagClass;
    var $deleteHandler = settings.deleteHandler;

    var $selectObj = $("#" + $selectId);
    var $tagContainerObj = $("#" + $tagContainerId);

    this._addTag = function(id, name) {

        if ($this._tagExists(id)) {
            return;
        }

        var addTagRow = false;

        var containerChildren = $tagContainerObj.children("div[class='row']");
        var tagRow = null;

        if (containerChildren.length == 0) {
            tagRow = $("<div class=\"row\" style=\"margin-left: 0px;\"></div>");
            addTagRow = true;
        } else {
            tagRow = containerChildren.first();
        }

        //console.log("tagRow: " + tagRow.html());

        var tag = $("<div class=\"\"></div>").addClass($tagClass);
        var span = $("<span></span>").append(name);
        var img = $("<img src=\"" + CONTEXT_PATH + "/resources/images/delete_16x16.png\" />").attr({
            title : $deleteTooltip
        });
        img.click(function() {
            $this._deleteTag(this);
        });
        var hidden = $("<input type=\"hidden\" />").attr({
            name : $paramName,
            value : id
        });
        var clear = $("<div></div>").css("clear", "both");



        tag = tag.append(span);
        tag = tag.append(img);
        tag = tag.append(hidden);
        tag = tag.append(clear);

        tagRow = tagRow.append(tag);

        //console.log("addTagRow: " + addTagRow);
        if (addTagRow) {
            $tagContainerObj = $tagContainerObj.append(tagRow);
        }

        //console.log("tagContainerObj: " + $tagContainerObj.html());

    }

    this._deleteTag = function(source) {

        var doDelete = true;

        var img = $(source);
        var tag = img.parent("div");

        /*If delete handler is configured in the settings, we call it and compute the result for processing delete.*/
        if ($deleteHandler != null) {
            doDelete = $deleteHandler(tag);
        }

        if (doDelete == true) {
            tag.remove();
        }

    }

    this._tagExists = function(id) {
        var containerChildren = $tagContainerObj.find("input:hidden[value='" + id + "']");

        return containerChildren.length > 0;
    }

    this.addTagById = function(id) {
        var name = $("#" + $selectId + " option[value='" + id + "']").text();

        $this._addTag(id, name);
    }

    $selectObj.change(function () {
        var id = $("#" + $selectId + " option:selected").val();
        var name = $("#" + $selectId + " option:selected").text();

        $this._addTag(id, name);

        $("#" + $selectId + " :nth-child(1)").prop('selected', true);
    });
}
