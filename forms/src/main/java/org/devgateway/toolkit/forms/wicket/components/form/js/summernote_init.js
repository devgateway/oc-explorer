;(function() {
    var summernoteConfig = ${summernoteconfig};
    var summernote = $('#'+summernoteConfig.summernoteEditorId);

    var toolbar = [];
    $.each(summernoteConfig.ToolbarOptions, function(key, value) {
        var category = [];
        category.push(key);
        category.push(value);
        toolbar.push(category);
    });

    var summernoteConfigDefault = {
		callbacks : {
		    //show show an alert box with the message
		    onImageUploadError: function(msg) {
		        alert(msg);
		    },
			onImageUpload : function(files) {
	            var files = $(files);
	            var filesSize = files.length;
	            var overlay;
	
	            // Show Overlay
	            var overlayTimeout = setTimeout(function() {
	                overlay = $("<div class='summernoteOverlay'></div>").appendTo("body");
	                new Spinner({color:'#fff'}).spin(overlay[0]);
	            }, summernoteConfig.overlayTimeout);
	
	            files.each(function() {
	                var file = this;
                     //check file size before upload
	                 if (summernoteConfig.maximumImageFileSize && summernoteConfig.maximumImageFileSize < file.size) {
	                          summernote.summernote('triggerEvent', 'image.upload.error',
	                          'Maximum image size allowed is '+summernoteConfig.maximumImageFileSize/1024+' KB!');
                                clearTimeout(overlayTimeout);
    	                              if(overlay) {
    	                                overlay.remove();
    	                            }
	                          return;
                     }

	                var data = new FormData();
	                data.append("file", file);
	                url = summernoteConfig.imageUploadUrl;
	                $.ajax({
	                    data : data,
	                    headers : {
	                        "Wicket-Ajax" : "true",
	                        "Wicket-Ajax-BaseURL" : Wicket.Ajax.baseUrl
	                    },
	                    type : "POST",
	                    url : url,
	                    cache : false,
	                    contentType : false,
	                    processData : false,
	                    success : function(res, status, xhr) {
	                        // Insert image
	                        var imageUrl = xhr.getResponseHeader("imageUrl");
	                        var decodedImageUrl = window.atob(/(image=)(.*)[^&]*/.exec(imageUrl)[2]);
	                        imageUrl = imageUrl.replace(/(image=)[^&]*/, '$1' + decodedImageUrl);
	                        $('#'+summernoteConfig.summernoteEditorId).summernote('insertImage', imageUrl, function($image){
	                        	// after image load but image is not inserted yet.
	                        	// https://github.com/summernote/summernote/issues/1472
    	                        // Hide Overlay
    	                        filesSize -= 1;
    	                        if (!filesSize) {
    	                            clearTimeout(overlayTimeout);
    	                            if(overlay) {
    	                                overlay.remove();
    	                            }
    	                        }
	                        });

	                    }
	                });
	            });
	        }
        },
        toolbar : toolbar
    };
    $.extend(summernoteConfigDefault, summernoteConfig);
    summernote.summernote(summernoteConfigDefault);
})();
