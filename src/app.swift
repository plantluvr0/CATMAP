@main
        import SwiftUI
        import MapKit
struct InteractiveMapView: View {

    // A model for a single point of interest
    struct Location: Identifiable {
        let id = UUID()
        let name: String
        let coordinate: CLLocationCoordinate2D
    }

    // 1. STATE: Tracks which location is currently selected
    @State private var selectedLocation: Location? = nil

    // Define the region (center and span/zoom) as a @State variable
    @State private var region = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 34.0522, longitude: -118.2437), // Los Angeles, CA
        span: MKCoordinateSpan(latitudeDelta: 0.1, longitudeDelta: 0.1) // Zoom level
    )


    let map = getMap()
    let annotations
    let nodes = map.getNodes()
    for node in nodes {
        if (node.isBuliding() == true) {
            annotations.add(Location(name: node.getName(),
                coordinate: CLLocationCoordinate2D(latitude: node.getXcord(), node.getYcord())))
        }
    }

    var body: some View {
        // Use a ZStack to layer the detail box on top of the map
        ZStack(alignment: .bottom) {

            // Use the Map view, binding it to the defined region
            Map(coordinateRegion: $region, annotations) { location in
                MapAnnotation(coordinate: location.coordinate) {
                    Image(systemName: "mappin.circle.fill")
                    .foregroundColor(self.selectedLocation?.id == location.id ? .blue : .red)
                    .scaleEffect(self.selectedLocation?.id == location.id ? 1.5 : 1.0)
                    .animation(.easeOut(duration: 0.2), value: self.selectedLocation)
                    .onTapGesture {
                        // 4. ACTION: When tapped, set the selectedLocation state
                        self.selectedLocation = location
                    }
                }
            }
            .ignoresSafeArea()// Optional: Extends map to fill the whole screen

            if let location = selectedLocation {
                DetailCalloutView(location: location) {
                    // Action for the button inside the detail box
                    print("Button tapped for \(location.name)")
                }
                .transition(.move(edge: .bottom)) // Makes it slide in/out
                .animation(.default, value: selectedLocation)
            }
        }
    }
}

func getMap() -> Map {
    // 1. Data structure you expect back (using Codable)
    struct MapResponse: Decodable {
        struct Node: Decodeable {
            let id: Int
            let name: String
            let xpos: Double
            let ypos: Double
            let neighbors: [Neighbor]
        }

        struct Neighbor: Decodeable {
            let id: Int
            let weight: Double
            let count: Int
        }

        let nodes: [Node]
    }

    let map
    let response = await fetchData()
    for strNode in response.nodes {
        let node = Node(id: strNode.id, name: strNode.name, xpos: strNode.xpos, ypos: strNode.ypos)
        for neighbor in strNode.neighbors {
            node.addNeighbor(id: neighbor.id, weight: neighbor.weight, count: neighbor.count)
        }
        map.addNode(node: node)
    }

    return map

    func fetchData() -> MapResponse async {
        // 2. Define the URL
        guard let url = URL(string: "https://api.example.com/data/123") else {
            print("Invalid URL")
            return
        }

        do {
            // 3. Perform the GET request (default method for data(from:))
            let (data, response) = try await URLSession.shared.data(from: url)

            // Optional: Check the HTTP status code
            guard let httpResponse = response as? HTTPURLResponse,
            httpResponse.statusCode == 200 else {
                print("Server error or unexpected status code.")
                return
            }

            // 4. Decode the JSON data into your Swift structure
            let decodedResponse = try JSONDecoder().decode(MapResponse.self, from: data)
            print("Successfully fetched data for ID: \(decodedResponse.id)")
            return decodedResponse

        } catch {
            // 5. Handle errors (network issues, decoding errors, etc.)
            print("Error fetching or decoding data: \(error)")
        }
    }

    // To run this function, you would call it from an async context:
    // Task {
    //     await fetchData()
    // }
}