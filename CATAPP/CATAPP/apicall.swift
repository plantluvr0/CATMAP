//
//  CATAPPApp.swift
//  CATAPP
//
//  Created by Ethan Stabenow on 12/5/25.
//

import Foundation

enum CATAPPError: Error {
    case invalidURL
    case dataFetchFailed
    case decodingFailed
}

struct MapResponse: Decodable {
    let nodes: [Node]
}

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

func getMap() async throws {

    
    // 2. Define the URL
    guard let url = URL(string: "https://api.example.com/data/123") else {
        throw CATAPPError.invalidURL
    }

    // 3. Perform the GET request (default method for data(from:))
    let (data, response) = try await URLSession.shared.data(from: url)

    // Optional: Check the HTTP status code
    guard let httpResponse = response as? HTTPURLResponse,
    httpResponse.statusCode == 200 else {
        throw  CATAPPError.dataFetchFailed
    }

    do {
        // 4. Decode the JSON data into your Swift structure
        let decodedResponse = try JSONDecoder().decode(MapResponse.self, from: data)
    } catch {
        print("Error fetching or decoding data: \(error)")
        throw CATAPPError.decodingFailed
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
