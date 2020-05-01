package guiclient

import guiclient.tools.Transform
import guiclient.tools.Vector3

interface ICamera<Context : IRenderingContext>{
    val transform : Transform
}