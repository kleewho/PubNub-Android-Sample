//
//  ViewController.swift
//  PubNubSample
//
//  Created by Luca Ventura on 10/6/20.
//  Copyright © 2020 Luca Ventura. All rights reserved.
//

import UIKit
import PubNub

class ViewController: UIViewController {
    @IBOutlet weak var countLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
    @IBOutlet weak var signalCountLbl: UILabel!
    
    let subKey = "INSERT_SUBSCRIBE_KEY"
    var pn: PubNub!
    var count = 0
    var signalCount = 0

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        if subKey == "INSERT_SUBSCRIBE_KEY" {
            fatalError("Enter subscribe key")
        } else {
            initPubNub()
        }
    }
    
    func initPubNub() {
        var config = PubNubConfiguration(publishKey: nil, subscribeKey: subKey)
        config.useSecureConnections = true
        config.uuid = UUID().uuidString
        config.cipherKey = Crypto(key: "key")
        PubNub.log.levels = [.error, .warn]

        self.pn = PubNub(configuration: config)

        let listener = SubscriptionListener(queue: .main)
        listener.didReceiveSubscription = { event in
            switch event {
            case .messageReceived(_):
                self.count += 1
                self.countLbl.text = "Count: \(self.count)"
                break
            case .connectionStatusChanged(let status):
                self.statusLbl.text = "Status: \(status)"
                break
            case .signalReceived(_):
                self.signalCount += 1
                self.signalCountLbl.text = "Signal Count: \(self.signalCount)"
                break
            default:
                break
            }
        }

        pn.add(listener)
        subscribeChannelGroup()
        //subscribeChannels()
        //subscribeToSignal()
    }
    
    func subscribeChannels() {
        var channels: [String] = []
        for i in 0...200 {
            channels.append("test_channel-\(i)")
            print("test_channel-\(i)")
        }

        pn.subscribe(to: channels)
    }

    func subscribeToSignal() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 4) {
            print("subscribing to signals")

            self.pn.subscribe(to: ["signal-\(25)"])
        }
    }

    func subscribeChannelGroup() {
        pn.listChannels(for: "cg_555") { (response) in
            let response = try! response.get()
            print(response.channels.count)

            self.pn.subscribe(to: [], and: ["cg_555"])
        }
    }
    
    @IBAction func resetCountTapped(_ sender: Any) {
        count = 0
        signalCount = 0
        self.countLbl.text = "Count: \(self.count)"
        self.signalCountLbl.text = "Signal Count: \(self.signalCount)"
    }
}
