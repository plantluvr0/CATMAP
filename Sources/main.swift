@main
import SwiftUI
import MapKit
import Foundation

// 1. Define the location data structure
struct Location: Identifiable {
    let id = UUID()
    let name: String
    let coordinate: CLLocationCoordinate2D
}

struct ContentView: View {
    
    // 2. Define the initial camera position for the map
    @State private var position: MapCameraPosition = .region(
        MKCoordinateRegion(
            center: CLLocationCoordinate2D(latitude: 40.6892, longitude: -74.0445), // Statue of Liberty coordinates
            span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05) // Zoom level
        )
    )
    
    // 3. Define the annotation(s) to place on the map
    let annotations = [
        Location(name: "Statue of Liberty", coordinate: CLLocationCoordinate2D(latitude: 40.6892, longitude: -74.0445))
    ]
    
    var body: some View {
        // 4. Create the Map view and pass the initial position
        Map(position: $position) {
            
            // 5. Add markers for the locations
            ForEach(annotations) { location in
                Marker(location.name, coordinate: location.coordinate)
            }
        }
        // This makes the map interactive (zoom, pan, tilt, rotate)
        .mapInteractionModes([.pan, .zoom, .rotate])
    }
}

// Optional: Preview code for Xcode
#Preview {
    ContentView()
}
