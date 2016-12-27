/**
 * Created by avalj on 12/26/16.
 */

// This example adds a search box to a map, using the Google Place Autocomplete
    // feature. People can enter geographical searches. The search box will return a
    // pick list containing a mix of places and predicted search terms.

    // This example requires the Places library. Include the libraries=places
    // parameter when you first load the API. For example:
    // <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">
var lat = 0;
var long = 0;

function initAutocomplete() {
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 32.775, lng: -96.796},
        zoom: 13,
        mapTypeId: 'roadmap'
    });
    var infowindow = new google.maps.InfoWindow();
    google.maps.event.addListener(map, 'click', function () {
        infowindow.close();
    });

    // Create the search box and link it to the UI element.
    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    // Bias the SearchBox results towards current map's viewport.
    map.addListener('bounds_changed', function () {
        searchBox.setBounds(map.getBounds());
    });

    var markers = [];
    // Listen for the event fired when the user selects a prediction and retrieve
    // more details for that place.
    searchBox.addListener('places_changed', function () {
        var places = searchBox.getPlaces();

        if (places.length == 0) {
            return;
        }

        // Clear out the old markers.
        markers.forEach(function (marker) {
            marker.setMap(null);
        });
        markers = [];

        // For each place, get the icon, name and location.
        var bounds = new google.maps.LatLngBounds();
        places.forEach(function (place) {
            if (!place.geometry) {
                console.log("Returned place contains no geometry");
                return;
            }
            var icon = {
                url: place.icon,
                size: new google.maps.Size(71, 71),
                origin: new google.maps.Point(0, 0),
                anchor: new google.maps.Point(17, 34),
                scaledSize: new google.maps.Size(25, 25)
            };

            // Create a marker for each place.
            markers.push(new google.maps.Marker({
                map: map,
                icon: icon,
                title: place.name,
                position: place.geometry.location
            }));

            lat = place.geometry.location.lat();
            long = place.geometry.location.lng();


            map.data.addListener('click', function (event) {
                var myHTML = "";
                event.feature.forEachProperty(function (value, property) {
                    myHTML = myHTML + "<b>" + property.toUpperCase() + "</b>" + ":" + value + "\n";
                    infowindow.setContent("<div style='width:150px; text-align: center;'>" + myHTML + "<br>" + "</div>");
                });

                infowindow.setPosition(event.feature.getGeometry().get());
                infowindow.setOptions({pixelOffset: new google.maps.Size(0, -30)});
                infowindow.open(map);
            });
            if (confirm("Press Ok for the latest prediction.[May take 3-4 minutes]") == true) {
                map.data.loadGeoJson('http://localhost:8080/location/' + lat + '/' + long + '/' + true);
            }
            else {
                //alert("Sorry then!!")
                map.data.loadGeoJson('http://localhost:8080/location/' + lat + '/' + long + '/' + false);

            }

            var featureStyle = {
                strokeColor: '#ff3333',
                strokeWeight: 4
            };
            map.data.setStyle(featureStyle);

            if (place.geometry.viewport) {
                // Only geocodes have viewport.
                bounds.union(place.geometry.viewport);
            } else {
                bounds.extend(place.geometry.location);
            }
        });
        map.fitBounds(bounds);
    });


}

