@main
import SwiftUI
import MapKit

struct InteractiveMapView: View {

    // 1. STATE: Variable to hold the calculated route line
    @State private var pathPolyline: MKPolyline? = nil

    // Define the region (center and span/zoom) as a @State variable
    @State private var region = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 34.0522, longitude: -118.2437), // Los Angeles, CA
        span: MKCoordinateSpan(latitudeDelta: 0.1, longitudeDelta: 0.1) // Zoom level
    )

    @State private var map = getMap()
    let nodes = map.getNodes()

    let annotations
    for node in nodes {
        if (node.isBuliding() == true) {
            annotations.add(Location(name: node.getName(),
                coordinate: CLLocationCoordinate2D(latitude: node.getXcord(), node.getYcord())))
        }
    }

    // A model for a single point of interest

    struct Location: Identifiable {
        let id = UUID()
        let name: String
        let coordinate: CLLocationCoordinate2D
    }

    struct DetailCalloutView: View {
        let location: Location
        let buttonAction: () -> Void

        var body: some View {
            HStack {
                // 1. Button (on the left)
                Button(action: buttonAction) {
                    Text("Start Route").padding().background(Color.blue).foregroundColor(.white).cornerRadius(8)
                    TwoWayAstar.findpath(start: ,)
                    buttonAction()
                }

                // 2. Text (to the right of the button)
                VStack(alignment: .leading) {
                    Text(location.name).font(.headline)
                    Text(location.detailText ?? "No details provided.").font(.subheadline).foregroundColor(.gray)
                }.padding(.leading)

                Spacer() // Pushes the content to the left
            }.padding().background(Color.white).cornerRadius(12).shadow(radius: 5).padding(.horizontal).padding(.bottom, 20) // Lifts it slightly above the bottom edge
        }
    }

    var body: some View {
        // Use a ZStack to layer the detail box on top of the map
        ZStack(alignment: .bottom) {

            // Use the Map view, binding it to the defined region
            Map(coordinateRegion: $region, annotations,
                // RENDER THE OVERLAY:
                overlayContent: { overlay in
                // Check if the overlay is the polyline we created
                if let polyline = pathPolyline, overlay as? MKPolyline == polyline {
                    // MKPolylineRenderer is used to style the line
                    MKPolylineRenderer(polyline: polyline).strokeColor = .blue
                    MKPolylineRenderer(polyline: polyline).lineWidth = 5
                    }
                }) {
                location in
                MapAnnotation(coordinate: location.coordinate) {
                    Image(systemName: "mappin.circle.fill").foregroundColor(self.selectedLocation ?.id == location.id ? .blue: .red).scaleEffect(self.selectedLocation ?.id == location.id ? 1.5: 1.0).animation(.easeOut(duration: 0.2), value: self.selectedLocation).onTapGesture {
                        // 4. ACTION: When tapped, set the selectedLocation state
                        self.selectedLocation = location
                    }
                }
            }.ignoresSafeArea()// Optional: Extends map to fill the whole screen

            if let location = selectedLocation {
                DetailCalloutView(location: location) {
                    // Action for the button inside the detail box
                    print("Button tapped for \(location.name)")
                }.transition(.move(edge: .bottom)) // Makes it slide in/out.animation(.default, value: selectedLocation)
            }
        }
    }

    func createPolylineFromNodes(nodes: [Node]) {
        // 1. Extract the coordinates from the ordered annotations array
        let coordinates = nodes.map { $0.coordinate }

        // 2. Convert the Swift array into a C-style pointer for MKPolyline
        let polyline = MKPolyline(coordinates: coordinates, count: coordinates.count)

        // 3. Update the state
        self.pathPolyline = polyline
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