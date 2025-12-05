@main
import SwiftUI
import MapKit

struct InteractiveMapView: View {
    struct Location: Identifiable {
        let id = UUID()
        let name: String
        let coordinate: CLLocationCoordinate2D
    }

    @State private var selectedStartLocation: Location? = nil
    @State private var selectedEndLocation: Location? = nil
    @State private var pathLocations: [Location] = []
    @State private var region = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 34.0522, longitude: -118.2437),
        span: MKCoordinateSpan(latitudeDelta: 0.1, longitudeDelta: 0.1)
    )

    @State private var annotations: [Location] = []
    @State private var mapData: Map? = nil

    var body: some View {
        ZStack(alignment: .bottom) {
            Map(coordinateRegion: $region, annotationItems: annotations) { location in
                MapAnnotation(coordinate: location.coordinate) {
                    Image(systemName: "mappin.circle.fill")
                        .foregroundColor(getAnnotationColor(for: location))
                        .scaleEffect(isSelectedLocation(location) ? 1.5 : 1.0)
                        .onTapGesture {
                            selectLocation(location)
                        }
                }
            }
            .ignoresSafeArea()

            // Draw path line
            if !pathLocations.isEmpty {
                MapPolyline(coordinates: pathLocations.map { $0.coordinate })
                    .stroke(.blue, lineWidth: 3)
            }

            VStack(spacing: 16) {
                HStack {
                    VStack(alignment: .leading) {
                        Text("Start: \(selectedStartLocation?.name ?? "None")")
                        Text("End: \(selectedEndLocation?.name ?? "None")")
                    }
                    Spacer()
                    Button("Clear") {
                        clearSelection()
                    }
                }
                .padding()
                .background(.white)
                .cornerRadius(8)

                if selectedStartLocation != nil && selectedEndLocation != nil {
                    Button("Find Path") {
                        findPath()
                    }
                    .buttonStyle(.borderedProminent)
                }
            }
            .padding()
        }
        .onAppear {
            loadMapData()
        }
    }

    private func selectLocation(_ location: Location) {
        if selectedStartLocation == nil {
            selectedStartLocation = location
        } else if selectedEndLocation == nil && selectedStartLocation?.id != location.id {
            selectedEndLocation = location
        }
    }

    private func findPath() {
        guard let start = selectedStartLocation, let end = selectedEndLocation, let map = mapData else { return }
        
        let startNode = map.getNodes().first { $0.getName() == start.name }
        let endNode = map.getNodes().first { $0.getName() == end.name }
        
        guard let startNode = startNode, let endNode = endNode else { return }
        
        let path = map.twoWayAStar(from: startNode, to: endNode)
        pathLocations = path.map { node in
            Location(name: node.getName(), coordinate: CLLocationCoordinate2D(latitude: node.getXcord(), longitude: node.getYcord()))
        }
    }

    private func clearSelection() {
        selectedStartLocation = nil
        selectedEndLocation = nil
        pathLocations = []
    }

    private func getAnnotationColor(for location: Location) -> Color {
        if selectedStartLocation?.id == location.id { return .green }
        if selectedEndLocation?.id == location.id { return .red }
        return .blue
    }

    private func isSelectedLocation(_ location: Location) -> Bool {
        selectedStartLocation?.id == location.id || selectedEndLocation?.id == location.id
    }

    private func loadMapData() {
        Task {
            let map = await getMap()
            self.mapData = map
            let nodes = map.getNodes()
            self.annotations = nodes.filter { $0.isBuilding() }.map { node in
                Location(name: node.getName(), coordinate: CLLocationCoordinate2D(latitude: node.getXcord(), longitude: node.getYcord()))
            }
        }
    }
}

func getMap() async -> Map {
    struct MapResponse: Decodable {
        struct Node: Decodable {
            let id: Int
            let name: String
            let xpos: Double
            let ypos: Double
            let neighbors: [Neighbor]
        }
        struct Neighbor: Decodable {
            let id: Int
            let weight: Double
            let count: Int
        }
        let nodes: [Node]
    }

    let map = Map()
    let response = await fetchData()
    
    for strNode in response.nodes {
        let node = Node(id: strNode.id, name: strNode.name, xpos: strNode.xpos, ypos: strNode.ypos)
        for neighbor in strNode.neighbors {
            node.addNeighbor(id: neighbor.id, weight: neighbor.weight, count: neighbor.count)
        }
        map.addNode(node: node)
    }
    return map

    func fetchData() async -> MapResponse {
        guard let url = URL(string: "https://api.example.com/data/123") else {
            return MapResponse(nodes: [])
        }
        do {
            let (data, response) = try await URLSession.shared.data(from: url)
            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                return MapResponse(nodes: [])
            }
            return try JSONDecoder().decode(MapResponse.self, from: data)
        } catch {
            print("Error: \(error)")
            return MapResponse(nodes: [])
        }
    }
}