//
//  CATAPPApp.swift
//  CATAPP
//
//  Created by Ethan Stabenow on 12/5/25.
//

import Foundation

func getMap() -> async Map {
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

    
    // 2. Define the URL
    guard let url = URL(string: "https://api.example.com/data/123") else {
        print("Invalid URL")
        return nil
    }

    do {
        // 3. Perform the GET request (default method for data(from:))
        let (data, response) = try await URLSession.shared.data(from: url)

        // Optional: Check the HTTP status code
        guard let httpResponse = response as? HTTPURLResponse,
        httpResponse.statusCode == 200 else {
            print("Server error or unexpected status code.")
            return nil
        }

        // 4. Decode the JSON data into your Swift structure
        let decodedResponse = try JSONDecoder().decode(MapResponse.self, from: data)

    } catch {
        // 5. Handle errors (network issues, decoding errors, etc.)
        print("Error fetching or decoding data: \(error)")
    }
    
    //let map
    //let response = await fetchData()
    //for strNode in response.nodes {
    //    let node = Node(id: strNode.id, name: strNode.name, xpos: strNode.xpos, ypos: strNode.ypos)
    //    for neighbor in strNode.neighbors {
    //        node.addNeighbor(id: neighbor.id, weight: neighbor.weight, count: neighbor.count)
    //    }
    //    map.addNode(node: node)
    //}

    //return map

}
