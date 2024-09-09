import { Control, FieldValues, Path } from "react-hook-form";
import { FormItem, FormLabel, FormControl, FormMessage, FormField } from "@/app/_components/ui/form";
import { Input } from "@/app/_components/ui/input";

interface IEmailFormControlProps<T extends FieldValues> {
  control: Control<T>;
  name: Path<T>;
}

const EmailFormControl = <T extends FieldValues>({ control, name }: IEmailFormControlProps<T>) => (
  <FormField
    name={name}
    control={control}
    render={({ field }) => (
      <FormItem>
        <FormLabel>Email</FormLabel>
        <FormControl>
          <Input size={33} className="text-center" {...field} />
        </FormControl>
        <FormMessage />
      </FormItem>
    )}
  />
);

export default EmailFormControl;
