import SwiftUI
import MapKit
import Foundation

//Structs
struct Location: Identifiable {
    let id = UUID()
    let name: String
    let coordinate: CLLocationCoordinate2D
}

//vars
let mapCamera = MapCamera(centerCoordinate: CLLocationCoordinate2D(latitude: 44.47601, longitude: -73.19649), distance: 0, heading: 0, pitch: 0)
var locations: [Location] = [Location(name: "davis", coordinate: CLLocationCoordinate2D(latitude: 44.47601, longitude: -73.19649)), ]

//Views
struct MapView: View {
    @State private var selectedLocationID: UUID?
    @State private var ploylineDestination: Location?
    @State private var mapCamera: MapCameraPosition = .userLocation(followsHeading: true, fallback: .automatic)
    
    
    var body: some View {
        // 4. Create the Map view and pass the initial position
        ZStack(alignment: .bottom) {
            Map(
                position: $mapCamera,
                bounds: MapCameraBounds(
                    centerCoordinateBounds: MKCoordinateRegion(
                        center: CLLocationCoordinate2D(
                            latitude: 44.47601,
                            longitude: -73.19649),
                        span: MKCoordinateSpan(
                            latitudeDelta: 0.005000,
                            longitudeDelta: 0.005000)),
                    minimumDistance: 500,
                    maximumDistance: 1000),
                interactionModes: MapInteractionModes.all,
                selection: $selectedLocationID.animation(),
                scope: Namespace().wrappedValue) {
                    
                    //set marks
                    ForEach (locations) { location in
                        Marker(location.name, coordinate: location.coordinate).tint(Color.blue).tag(location.id)
                    }
                    
                    //set polyline
                    
                    
            }
            
            if let selectedLocationID, let location = locations.first(where: { $0.id == selectedLocationID}) {
                VStack {
                    Text(location.name)
                    HStack {
                        Button {
                            //astar
                            
                        } label: {
                            Text("Directions")
                        }
                        
                    }
                }
                .frame(width: 400, height: 200)
                .background(Color.white)
                .transition(.move(edge: .bottom))
            }
        }
    }
}

@main
struct CATMAP: App {
    var body: some Scene {
        WindowGroup {
            MapView()
        }
    }
}

// Optional: Preview code for Xcode
#Preview {
    MapView()
}
