package cats.effect

// copy from scala.runtime.PStatics
private[effect] object PlatformStatics {
  // `Int.MaxValue - 8` traditional soft limit to maximize compatibility with diverse JVMs
  // See https://stackoverflow.com/a/8381338 for example
  final val VM_MaxArraySize = 2147483639
}
