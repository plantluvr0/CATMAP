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
    var body: some View {
        // 4. Create the Map view and pass the initial position
        Map() {
        }
    }
}

struct CATMAP: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

// Optional: Preview code for Xcode
#Preview {
    ContentView()
}
